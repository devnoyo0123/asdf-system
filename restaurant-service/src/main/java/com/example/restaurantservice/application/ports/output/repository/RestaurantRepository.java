package com.example.restaurantservice.application.ports.output.repository;

import com.example.restaurantservice.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {
    Optional<Restaurant> findOneBy(UUID restaurantId, List<UUID> productIds);
}
