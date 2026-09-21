package com.ordriva.orders.domain;

import com.ordriva.common.domain.OrderStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "order_status_history", indexes = {
        @Index(name = "idx_order_history_order_time", columnList = "order_id, occurred_at")
})
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(length = 500)
    private String reason;

    protected OrderStatusHistory() {
    }

    public OrderStatusHistory(CustomerOrder order, OrderStatus status, String reason) {
        this.order = order;
        this.status = status;
        this.reason = reason;
        this.occurredAt = Instant.now();
    }

    public OrderStatus getStatus() { return status; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getReason() { return reason; }
}