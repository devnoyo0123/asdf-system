package com.example.orderservice.application;

import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.modulecommon.domain.valueobject.OrderStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.application.ports.output.order.repository.OrderRepository;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {
    private final OrderRepository orderRepository;


    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrder(String orderId) {
        var orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if( orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id: " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    void saveOrder(Order order) {
        orderRepository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case PAID:
                return SagaStatus.PROCESSING;
            case APPROVED:
                return SagaStatus.SUCCEEDED;
            case CANCELLING:
                return SagaStatus.COMPENSATING;
            case CANCELLED:
                return SagaStatus.COMPENSATED;
            default:
                return SagaStatus.FAILED;
        }
    }
}
