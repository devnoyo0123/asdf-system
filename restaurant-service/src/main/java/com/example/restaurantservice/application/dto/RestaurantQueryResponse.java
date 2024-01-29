package com.example.restaurantservice.application.dto;

import com.example.restaurantservice.domain.entity.Product;

import java.util.List;
import java.util.UUID;

public record RestaurantQueryResponse(
        UUID id,
        List<ProductDto> products,
        boolean active
) {

    static public RestaurantQueryResponse of(UUID id, List<ProductDto> products, boolean active) {
        return new RestaurantQueryResponse(id, products, active);
    }
}
