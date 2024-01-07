package com.example.orderservice.application;

import com.example.orderservice.application.dto.message.RestaurantApproveResponse;
import com.example.orderservice.application.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.example.orderservice.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;

    public RestaurantApprovalResponseMessageListenerImpl(OrderApprovalSaga orderApprovalSaga) {
        this.orderApprovalSaga = orderApprovalSaga;
    }

    @Override
    public void orderApproved(RestaurantApproveResponse response) {
        orderApprovalSaga.process(response);
        log.info("Order is approved for order id: {}", response.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApproveResponse response) {
        OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(response);
        log.info("Publishing order cancelled event for order id: {} with failure messages: {}",
                response.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMITER, response.getFailureMessages()));
        orderCancelledEvent.fire();
    }
}
