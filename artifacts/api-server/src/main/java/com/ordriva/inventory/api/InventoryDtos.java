package com.ordriva.inventory.api;

import java.time.Instant;

public final class InventoryDtos {
    private InventoryDtos() {
    }

    public record InventoryResponse(
            Long id,
            Long productId,
            String product,
            String sku,
            Long warehouseId,
            String warehouse,
            int availableQuantity,
            int reservedQuantity,
            int reorderLevel,
            String status,
            long version,
            Instant updatedAt
    ) {
    }
}