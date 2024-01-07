package com.example.restaurantservice.domain.event;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.example.restaurantservice.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent{

    private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher;

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              OrderRejectedMessagePublisher orderRejectedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderRejectedEventDomainEventPublisher = orderRejectedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderRejectedEventDomainEventPublisher.publish(this);
    }
}
