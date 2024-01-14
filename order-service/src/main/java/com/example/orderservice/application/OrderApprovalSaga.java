package com.example.orderservice.application;

import com.example.modulecommon.domain.event.EmptyEvent;
import com.example.modulecommon.saga.SagaStep;
import com.example.orderservice.application.dto.message.RestaurantApproveResponse;
import com.example.orderservice.domain.OrderDomainService;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApproveResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;


    public OrderApprovalSaga(OrderDomainService orderDomainService,
                             OrderSagaHelper orderSagaHelper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
    }


    @Override
    @Transactional
    public void process(RestaurantApproveResponse restaurantApproveResponse) {
        log.info("Approving order with id: {}", restaurantApproveResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApproveResponse.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} approved", restaurantApproveResponse.getOrderId());
    }

    @Override
    @Transactional
    public void rollback(RestaurantApproveResponse restaurantApproveResponse) {
        log.info("Cancelling order with id: {}", restaurantApproveResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApproveResponse.getOrderId());
        orderDomainService.cancelOrderPayment(order,
                restaurantApproveResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} cancelled", restaurantApproveResponse.getOrderId());
    }
}
