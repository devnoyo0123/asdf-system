package com.example.restaurantservice.application;

import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.application.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import org.springframework.stereotype.Service;

@Service
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    public RestaurantApprovalRequestMessageListenerImpl(RestaurantApprovalRequestHelper restaurantApprovalRequestHelper) {
        this.restaurantApprovalRequestHelper = restaurantApprovalRequestHelper;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        var orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
//        orderApprovalEvent.fire();
    }
}
