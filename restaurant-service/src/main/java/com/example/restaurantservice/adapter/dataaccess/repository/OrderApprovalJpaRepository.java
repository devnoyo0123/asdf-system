package com.example.restaurantservice.adapter.dataaccess.repository;

import com.example.restaurantservice.adapter.dataaccess.entity.OrderApprovalEntity;
import com.example.restaurantservice.domain.entity.OrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {
    Optional<OrderApprovalEntity> findOneByOrderIdAndRestaurantId(UUID orderId, UUID RestaurantId);
}
