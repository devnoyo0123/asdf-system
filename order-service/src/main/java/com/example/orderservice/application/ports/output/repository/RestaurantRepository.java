package com.example.orderservice.application.ports.output.repository;

import com.example.orderservice.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
