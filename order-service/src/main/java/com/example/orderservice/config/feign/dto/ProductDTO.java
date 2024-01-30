package com.example.orderservice.config.feign.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull BigDecimal price,
        @NotNull boolean isAvailable
) {
}
