package com.example.orderservice.domain.event;

import com.example.modulecommon.domain.event.DomainEvent;
import com.example.orderservice.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
