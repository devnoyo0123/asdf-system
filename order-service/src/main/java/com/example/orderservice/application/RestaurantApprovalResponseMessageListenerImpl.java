package com.example.orderservice.application;

import com.example.orderservice.application.dto.message.RestaurantApproveResponse;
import com.example.orderservice.application.ports.input.service.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApproveResponse response) {

    }

    @Override
    public void orderRejected(RestaurantApproveResponse response) {

    }
}
