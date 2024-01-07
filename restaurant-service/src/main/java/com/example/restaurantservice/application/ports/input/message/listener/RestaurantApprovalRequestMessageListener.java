package com.example.restaurantservice.application.ports.input.message.listener;

import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
