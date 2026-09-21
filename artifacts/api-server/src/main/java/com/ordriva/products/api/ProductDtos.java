package com.ordriva.products.api;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public final class ProductDtos {
    private ProductDtos() {
    }

    public record ProductResponse(
            Long id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record ProductRequest(
            @NotBlank @Size(max = 80) String sku,
            @NotBlank @Size(max = 180) String name,
            @Size(max = 5000) String description,
            @NotNull @Positive BigDecimal price,
            boolean active
    ) {
    }
}