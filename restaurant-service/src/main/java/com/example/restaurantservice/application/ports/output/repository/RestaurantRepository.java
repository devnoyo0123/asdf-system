package com.example.restaurantservice.application.ports.output.repository;

import com.example.restaurantservice.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
