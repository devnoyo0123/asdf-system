package com.example.restaurantservice.application.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantQuery(
        UUID id,
        List<UUID> productIds
) {

        static public RestaurantQuery of(UUID id, List<UUID> productIds) {
            return new RestaurantQuery(id, productIds);
        }
}
