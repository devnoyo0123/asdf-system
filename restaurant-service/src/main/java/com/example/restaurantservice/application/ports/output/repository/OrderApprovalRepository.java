package com.example.restaurantservice.application.ports.output.repository;

import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.domain.entity.OrderApproval;

import java.util.Optional;
import java.util.UUID;

public interface OrderApprovalRepository {
    OrderApproval persist(OrderApproval orderApproval);
    Optional<OrderApproval> findOneBy(OrderId orderId , RestaurantId restaurantId);
}
