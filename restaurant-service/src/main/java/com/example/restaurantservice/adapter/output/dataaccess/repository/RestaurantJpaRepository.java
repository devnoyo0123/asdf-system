package com.example.restaurantservice.adapter.output.dataaccess.repository;


import com.example.restaurantservice.adapter.output.dataaccess.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID>, RestaurantCustomRepository {
}
