package com.ordriva.inventory.api;

import com.ordriva.common.api.ApiResponse;
import com.ordriva.inventory.api.InventoryDtos.InventoryResponse;
import com.ordriva.inventory.service.InventoryService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<java.util.List<InventoryResponse>> list(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 25, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<InventoryResponse> page = service.list(productId, warehouseId, search, pageable);
        return ApiResponse.page(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ApiResponse<InventoryResponse> get(@PathVariable Long id) {
        return ApiResponse.of(service.get(id));
    }
}