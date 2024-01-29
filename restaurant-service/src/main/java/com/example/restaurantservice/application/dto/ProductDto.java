package com.example.restaurantservice.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        BigDecimal price,
        boolean isAvailable
) {
    static public ProductDto of(UUID id, String name, BigDecimal price, boolean isAvailable) {
        return new ProductDto(id, name, price, isAvailable);
    }
}
