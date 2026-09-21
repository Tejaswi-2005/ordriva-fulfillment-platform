package com.ordriva.warehouses.api;

import com.ordriva.common.api.ApiResponse;
import com.ordriva.warehouses.api.WarehouseDtos.*;
import com.ordriva.warehouses.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/warehouses")
public class WarehouseController {
    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<java.util.List<WarehouseResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 25, sort = "name") Pageable pageable
    ) {
        Page<WarehouseResponse> page = service.list(search, active, pageable);
        return ApiResponse.page(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseResponse> get(@PathVariable Long id) {
        return ApiResponse.of(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER', 'WAREHOUSE_MANAGER')")
    public ApiResponse<WarehouseResponse> create(@Valid @RequestBody WarehouseRequest request) {
        return ApiResponse.of(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER', 'WAREHOUSE_MANAGER')")
    public ApiResponse<WarehouseResponse> update(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        return ApiResponse.of(service.update(id, request));
    }
}