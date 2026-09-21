package com.ordriva.orders.service;

import com.ordriva.common.api.ConflictException;
import com.ordriva.common.api.ResourceNotFoundException;
import com.ordriva.common.domain.OrderStatus;
import com.ordriva.inventory.domain.Inventory;
import com.ordriva.inventory.repository.InventoryRepository;
import com.ordriva.orders.api.OrderDtos.*;
import com.ordriva.orders.domain.CustomerOrder;
import com.ordriva.orders.domain.OrderItem;
import com.ordriva.orders.domain.OrderStatusHistory;
import com.ordriva.orders.repository.OrderRepository;
import com.ordriva.orders.repository.OrderStatusHistoryRepository;
import com.ordriva.products.domain.Product;
import com.ordriva.products.repository.ProductRepository;
import com.ordriva.users.domain.User;
import com.ordriva.users.repository.UserRepository;
import com.ordriva.warehouses.domain.Warehouse;
import com.ordriva.warehouses.repository.WarehouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
    private static final Set<OrderStatus> CANCELLABLE = EnumSet.of(
            OrderStatus.CREATED,
            OrderStatus.INVENTORY_RESERVING,
            OrderStatus.INVENTORY_RESERVED,
            OrderStatus.PAYMENT_PENDING,
            OrderStatus.CONFIRMED,
            OrderStatus.PROCESSING
    );

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderStatusHistoryRepository historyRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + request.userId() + " was not found"));

        Map<Long, Integer> requestedQuantities = new LinkedHashMap<>();
        for (ItemRequest item : request.items()) {
            requestedQuantities.merge(item.productId(), item.quantity(), Integer::sum);
        }

        List<Product> products = requestedQuantities.keySet().stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " was not found")))
                .toList();
        if (products.stream().anyMatch(product -> !product.isActive())) {
            throw new ConflictException("Orders can only contain active products");
        }

        Warehouse warehouse = chooseWarehouse(request.warehouseId(), requestedQuantities);
        CustomerOrder order = new CustomerOrder(
                nextOrderNumber(),
                user,
                warehouse,
                BigDecimal.ZERO,
                OrderStatus.CREATED
        );
        historyRepository.save(new OrderStatusHistory(order, OrderStatus.CREATED, "Order accepted"));
        order.transitionTo(OrderStatus.INVENTORY_RESERVING);
        historyRepository.save(new OrderStatusHistory(order, OrderStatus.INVENTORY_RESERVING, "Reserving inventory"));

        BigDecimal total = BigDecimal.ZERO;
        for (Product product : products) {
            int quantity = requestedQuantities.get(product.getId());
            Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                    .orElseThrow(() -> new ConflictException("No inventory position exists for " + product.getSku()));
            if (!inventory.canReserve(quantity)) {
                throw new ConflictException("Insufficient inventory for " + product.getSku());
            }
            inventory.reserve(quantity);
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(lineTotal);
            order.addItem(new OrderItem(product, quantity, product.getPrice()));
        }

        order.transitionTo(OrderStatus.INVENTORY_RESERVED);
        historyRepository.save(new OrderStatusHistory(order, OrderStatus.INVENTORY_RESERVED, "Inventory reserved"));
        order.transitionTo(OrderStatus.PAYMENT_PENDING);
        historyRepository.save(new OrderStatusHistory(order, OrderStatus.PAYMENT_PENDING, "Waiting for payment authorization"));
        order = orderRepository.save(order);
        return toResponse(order, historyRepository.findByOrderIdOrderByOccurredAtAsc(order.getId()));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> list(String search, OrderStatus status, Long warehouseId, Pageable pageable) {
        return orderRepository.search(normalize(search), status, warehouseId, pageable)
                .map(order -> toResponse(order, historyRepository.findByOrderIdOrderByOccurredAtAsc(order.getId())));
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        CustomerOrder order = detailed(id);
        return toResponse(order, historyRepository.findByOrderIdOrderByOccurredAtAsc(id));
    }

    @Transactional(readOnly = true)
    public List<TimelineEntry> timeline(Long id) {
        detailed(id);
        return historyRepository.findByOrderIdOrderByOccurredAtAsc(id).stream()
                .map(history -> new TimelineEntry(history.getStatus(), history.getOccurredAt(), history.getReason()))
                .toList();
    }

    @Transactional
    public OrderResponse cancel(Long id, String reason) {
        CustomerOrder order = detailed(id);
        if (!CANCELLABLE.contains(order.getStatus())) {
            throw new ConflictException("Order " + order.getOrderNumber() + " cannot be cancelled from " + order.getStatus());
        }
        for (OrderItem item : order.getItems()) {
            inventoryRepository.findByProductIdAndWarehouseId(item.getProduct().getId(), order.getWarehouse().getId())
                    .ifPresent(inventory -> inventory.release(item.getQuantity()));
        }
        order.transitionTo(OrderStatus.CANCELLED);
        historyRepository.save(new OrderStatusHistory(
                order,
                OrderStatus.CANCELLED,
                reason == null || reason.isBlank() ? "Cancelled by operator" : reason.trim()
        ));
        return toResponse(order, historyRepository.findByOrderIdOrderByOccurredAtAsc(id));
    }

    private Warehouse chooseWarehouse(Long requestedWarehouseId, Map<Long, Integer> quantities) {
        List<Warehouse> candidates = requestedWarehouseId == null
                ? warehouseRepository.findAll().stream().filter(Warehouse::isActive).toList()
                : List.of(warehouseRepository.findById(requestedWarehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse " + requestedWarehouseId + " was not found")));
        return candidates.stream()
                .filter(warehouse -> quantities.entrySet().stream().allMatch(entry ->
                        inventoryRepository.findByProductIdAndWarehouseId(entry.getKey(), warehouse.getId())
                                .map(inventory -> inventory.canReserve(entry.getValue()))
                                .orElse(false)
                ))
                .findFirst()
                .orElseThrow(() -> new ConflictException("No active warehouse can fulfill the requested quantities"));
    }

    private CustomerOrder detailed(Long id) {
        return orderRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + id + " was not found"));
    }

    private String nextOrderNumber() {
        String orderNumber;
        do {
            orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        } while (orderRepository.findByOrderNumber(orderNumber).isPresent());
        return orderNumber;
    }

    private OrderResponse toResponse(CustomerOrder order, List<OrderStatusHistory> history) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getSku(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();
        List<TimelineEntry> timeline = history.stream()
                .map(item -> new TimelineEntry(item.getStatus(), item.getOccurredAt(), item.getReason()))
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getUser().getId(),
                order.getUser().getName(),
                order.getWarehouse().getId(),
                order.getWarehouse().getName() + " / " + order.getWarehouse().getCode(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items,
                timeline
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}