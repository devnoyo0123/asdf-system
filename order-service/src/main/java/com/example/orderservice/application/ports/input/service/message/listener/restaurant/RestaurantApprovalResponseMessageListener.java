package com.example.orderservice.application.ports.input.service.message.listener.restaurant;

import com.example.orderservice.application.dto.message.RestaurantApproveResponse;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantApproveResponse response);

    void orderRejected(RestaurantApproveResponse response);
}
