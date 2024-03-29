package com.example.restaurantservice.adapter.dataaccess.repository;

import com.example.restaurantservice.application.ports.output.repository.RestaurantRepository;
import com.example.restaurantservice.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public Optional<Restaurant> findOneBy(UUID restaurantId, List<UUID> productIds) {
        return  restaurantJpaRepository
                .findOneBy(restaurantId,
                        productIds);
    }
}
