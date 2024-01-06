package com.example.orderservice.domain.event;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.orderservice.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> domainEventPublisher;

    public OrderCancelledEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCancelledEvent> domainEventPublisher) {
        super(order, createdAt);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        this.domainEventPublisher.publish(this);
    }
}
