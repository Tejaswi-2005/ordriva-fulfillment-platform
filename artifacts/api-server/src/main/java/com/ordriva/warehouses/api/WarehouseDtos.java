package com.ordriva.warehouses.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class WarehouseDtos {
    private WarehouseDtos() {
    }

    public record WarehouseResponse(
            Long id,
            String name,
            String code,
            String city,
            String state,
            String country,
            boolean active
    ) {
    }

    public record WarehouseRequest(
            @NotBlank @Size(max = 140) String name,
            @NotBlank @Size(max = 30) String code,
            @NotBlank @Size(max = 100) String city,
            @NotBlank @Size(max = 100) String state,
            @NotBlank @Size(max = 100) String country,
            boolean active
    ) {
    }
}