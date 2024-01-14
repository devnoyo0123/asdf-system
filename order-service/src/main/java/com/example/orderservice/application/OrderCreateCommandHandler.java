package com.example.orderservice.application;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.scheduler.payment.PaymentOutboxHelper;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;


    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderSagaHelper orderSagaHelper, PaymentOutboxHelper paymentOutboxHelper) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderSagaHelper = orderSagaHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderCreatedEvent.getOrder().getOrderStatus(),
                orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID());

        log.info("Returning CreateOrderResponse with order id: {}", orderCreatedEvent.getOrder().getId().getValue());

        return createOrderResponse;
    }
}
