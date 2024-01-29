package com.example.orderservice.config.feign.dto;

import com.example.modulecommon.domain.entity.Product;

import java.util.List;

public record RestaurantDTO(
        List<Product> products,
        boolean active
) {
}
