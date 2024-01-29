package com.example.orderservice.application.ports.output.customer.executor;

import com.example.modulecommon.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantExecutor {
    Optional<Restaurant> getRestaurantByIdAndProductIdIn(Restaurant restaurant);
}
