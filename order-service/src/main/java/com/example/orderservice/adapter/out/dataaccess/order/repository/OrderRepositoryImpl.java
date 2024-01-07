package com.example.orderservice.adapter.out.dataaccess.order.repository;

import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.orderservice.adapter.out.dataaccess.order.mapper.OrderDataAccessMapper;
import com.example.orderservice.application.ports.output.order.repository.OrderJpaRepository;
import com.example.orderservice.application.ports.output.order.repository.OrderRepository;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }

}
