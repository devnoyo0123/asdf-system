package com.example.restaurantservice.domain.event;

import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.example.restaurantservice.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent{


    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
