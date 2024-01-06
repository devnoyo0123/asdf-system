package com.example.orderservice.domain.event;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.orderservice.domain.entity.Order;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> domainEventPublisher;

    public OrderPaidEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderPaidEvent> domainEventPublisher) {
        super(order, createdAt);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        this.domainEventPublisher.publish(this);
    }
}
