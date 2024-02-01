package com.example.orderservice.config.feign.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantDTO(
        UUID id,
        List<ProductDTO> products,
        boolean active
) {
}
