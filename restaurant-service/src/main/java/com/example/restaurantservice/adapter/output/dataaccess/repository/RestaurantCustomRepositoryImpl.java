package com.example.restaurantservice.adapter.output.dataaccess.repository;

import com.example.restaurantservice.adapter.output.dataaccess.entity.RestaurantEntity;
import com.example.restaurantservice.adapter.output.dataaccess.mapper.RestaurantDataAccessMapper;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import com.example.restaurantservice.domain.entity.Restaurant;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RestaurantCustomRepositoryImpl implements RestaurantCustomRepository{

    private final EntityManager entityManager;

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantCustomRepositoryImpl(EntityManager entityManager, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.entityManager = entityManager;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findOneBy(UUID id, List<UUID> productIds) {
        RestaurantEntity restaurantEntity = entityManager.createQuery(
                "select r from RestaurantEntity r" +
                        " join fetch r.restaurantProducts rp" +
                        " join fetch rp.product p" +
                        " where r.id = :id" +
                        " and p.id in :productIds", RestaurantEntity.class)
                .setParameter("id", id)
                .setParameter("productIds", productIds)
                .getSingleResult();

        return Optional.of(restaurantDataAccessMapper.restaurantEntityToRestaurant(restaurantEntity));
    }
}
