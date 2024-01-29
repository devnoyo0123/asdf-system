package com.example.orderservice.adapter.out.internalapi;

import com.example.orderservice.application.ports.output.customer.executor.RestaurantExecutor;
import com.example.modulecommon.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantExecutorImpl implements RestaurantExecutor {
    @Override
    public Optional<Restaurant> getRestaurantByIdAndProductIdIn(Restaurant restaurant) {
        return Optional.empty();
    }
}
