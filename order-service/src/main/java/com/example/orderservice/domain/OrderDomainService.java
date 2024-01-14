package com.example.orderservice.domain;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.entity.Restaurant;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import com.example.orderservice.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
