package com.example.orderservice.domain.event;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.orderservice.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> domainEventPublisher;


    public OrderCreatedEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCreatedEvent> domainEventPublisher) {
        super(order, createdAt);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        this.domainEventPublisher.publish(this);
    }
}
