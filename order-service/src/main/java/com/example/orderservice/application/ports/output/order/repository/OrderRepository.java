package com.example.orderservice.application.ports.output.order.repository;

import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
