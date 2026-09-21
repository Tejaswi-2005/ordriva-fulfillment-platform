package com.ordriva.fulfillment.domain;

import com.ordriva.common.domain.FulfillmentStatus;
import com.ordriva.orders.domain.CustomerOrder;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "fulfillments", indexes = {
        @Index(name = "idx_fulfillments_status", columnList = "status")
})
public class Fulfillment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private CustomerOrder order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private FulfillmentStatus status;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Fulfillment() {
    }

    public Fulfillment(CustomerOrder order, FulfillmentStatus status, String trackingNumber) {
        this.order = order;
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.updatedAt = Instant.now();
    }
}