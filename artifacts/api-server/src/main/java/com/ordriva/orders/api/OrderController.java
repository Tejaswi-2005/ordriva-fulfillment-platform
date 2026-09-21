package com.ordriva.orders.api;

import com.ordriva.common.api.ApiResponse;
import com.ordriva.common.domain.OrderStatus;
import com.ordriva.orders.api.OrderDtos.*;
import com.ordriva.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER', 'SUPPORT_AGENT')")
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.of(service.create(request));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long warehouseId,
            @PageableDefault(size = 25, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderResponse> page = service.list(search, status, warehouseId, pageable);
        return ApiResponse.page(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        return ApiResponse.of(service.get(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER', 'SUPPORT_AGENT')")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id, @RequestBody(required = false) CancelOrderRequest request) {
        return ApiResponse.of(service.cancel(id, request == null ? null : request.reason()));
    }

    @GetMapping("/{id}/timeline")
    public ApiResponse<List<TimelineEntry>> timeline(@PathVariable Long id) {
        return ApiResponse.of(service.timeline(id));
    }
}