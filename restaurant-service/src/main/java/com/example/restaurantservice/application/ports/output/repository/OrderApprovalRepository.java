package com.example.restaurantservice.application.ports.output.repository;

import com.example.restaurantservice.domain.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval persist(OrderApproval orderApproval);
}
