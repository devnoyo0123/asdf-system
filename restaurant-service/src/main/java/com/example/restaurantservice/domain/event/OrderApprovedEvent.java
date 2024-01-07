package com.example.restaurantservice.domain.event;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.example.restaurantservice.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent{

    private final OrderApprovedMessagePublisher orderApprovalEventDomainEventPublisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              OrderApprovedMessagePublisher orderApprovalEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderApprovalEventDomainEventPublisher = orderApprovalEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderApprovalEventDomainEventPublisher.publish(this);
    }
}
