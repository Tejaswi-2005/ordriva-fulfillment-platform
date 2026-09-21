package com.ordriva.orders.api;

import com.ordriva.common.domain.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class OrderDtos {
    private OrderDtos() {
    }

    public record CreateOrderRequest(
            @NotNull Long userId,
            Long warehouseId,
            @NotEmpty List<@Valid ItemRequest> items
    ) {
    }

    public record ItemRequest(
            @NotNull Long productId,
            @Positive int quantity
    ) {
    }

    public record OrderItemResponse(
            Long productId,
            String product,
            String sku,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {
    }

    public record TimelineEntry(
            OrderStatus status,
            Instant occurredAt,
            String reason
    ) {
    }

    public record OrderResponse(
            Long id,
            String orderNumber,
            Long userId,
            String customer,
            Long warehouseId,
            String warehouse,
            OrderStatus status,
            BigDecimal totalAmount,
            Instant createdAt,
            Instant updatedAt,
            List<OrderItemResponse> items,
            List<TimelineEntry> timeline
    ) {
    }

    public record CancelOrderRequest(@Size(max = 500) String reason) {
    }
}