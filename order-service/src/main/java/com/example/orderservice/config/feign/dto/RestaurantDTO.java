package com.example.orderservice.config.feign.dto;

import com.example.modulecommon.domain.entity.Product;

import java.util.List;
import java.util.UUID;

public record RestaurantDTO(
        UUID id,
        List<ProductDTO> products,
        boolean active
) {
}
