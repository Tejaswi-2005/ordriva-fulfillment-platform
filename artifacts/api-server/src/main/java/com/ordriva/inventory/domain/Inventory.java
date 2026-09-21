package com.ordriva.inventory.domain;

import com.ordriva.products.domain.Product;
import com.ordriva.warehouses.domain.Warehouse;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "inventory", uniqueConstraints = {
        @UniqueConstraint(name = "uq_inventory_product_warehouse", columnNames = {"product_id", "warehouse_id"})
}, indexes = {
        @Index(name = "idx_inventory_product", columnList = "product_id"),
        @Index(name = "idx_inventory_warehouse", columnList = "warehouse_id")
})
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Version
    @Column(nullable = false)
    private long version;

    @Column(name = "reorder_level", nullable = false)
    private int reorderLevel;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Inventory() {
    }

    public Inventory(Product product, Warehouse warehouse, int availableQuantity, int reservedQuantity, int reorderLevel) {
        this.product = product;
        this.warehouse = warehouse;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.reorderLevel = reorderLevel;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Warehouse getWarehouse() { return warehouse; }
    public int getAvailableQuantity() { return availableQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
    public long getVersion() { return version; }
    public int getReorderLevel() { return reorderLevel; }
    public Instant getUpdatedAt() { return updatedAt; }

    public boolean canReserve(int quantity) {
        return quantity > 0 && availableQuantity >= quantity;
    }

    public void reserve(int quantity) {
        if (!canReserve(quantity)) {
            throw new IllegalArgumentException("Insufficient inventory");
        }
        availableQuantity -= quantity;
        reservedQuantity += quantity;
        updatedAt = Instant.now();
    }

    public void release(int quantity) {
        if (quantity <= 0 || reservedQuantity < quantity) {
            throw new IllegalArgumentException("Cannot release more inventory than reserved");
        }
        availableQuantity += quantity;
        reservedQuantity -= quantity;
        updatedAt = Instant.now();
    }
}