package com.ordriva.inventory.service;

import com.ordriva.common.api.ResourceNotFoundException;
import com.ordriva.inventory.api.InventoryDtos.InventoryResponse;
import com.ordriva.inventory.domain.Inventory;
import com.ordriva.inventory.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<InventoryResponse> list(Long productId, Long warehouseId, String search, Pageable pageable) {
        return repository.search(productId, warehouseId, normalize(search), pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public InventoryResponse get(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory position " + id + " was not found")));
    }

    private InventoryResponse toResponse(Inventory i) {
        String status = i.getAvailableQuantity() == 0
                ? "OUT_OF_STOCK"
                : i.getAvailableQuantity() <= i.getReorderLevel() ? "LOW_STOCK" : "IN_STOCK";
        return new InventoryResponse(
                i.getId(),
                i.getProduct().getId(),
                i.getProduct().getName(),
                i.getProduct().getSku(),
                i.getWarehouse().getId(),
                i.getWarehouse().getName() + " / " + i.getWarehouse().getCode(),
                i.getAvailableQuantity(),
                i.getReservedQuantity(),
                i.getReorderLevel(),
                status,
                i.getVersion(),
                i.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}