package com.example.restaurantservice.adapter.dataaccess.repository;

import com.example.restaurantservice.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantCustomRepository {
    Optional<Restaurant> findOneBy(UUID id, List<UUID> productIds);
}
