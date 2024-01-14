package com.example.orderservice.application;

import com.example.modulecommon.domain.valueobject.OrderStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.modulecommon.saga.SagaStep;
import com.example.orderservice.application.dto.message.PaymentResponse;
import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.scheduler.approval.ApprovalOutboxHelper;
import com.example.orderservice.application.ports.output.scheduler.payment.PaymentOutboxHelper;
import com.example.orderservice.domain.OrderDomainService;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.event.OrderPaidEvent;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    public OrderPaymentSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper, ApprovalOutboxHelper approvalOutboxHelper, PaymentOutboxHelper paymentOutboxHelper, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(paymentResponse.getSagaId()),
                SagaStatus.STARTED
        );

        if(orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("Outbox message with saga id :{} is already processed!", paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

        log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
        var order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderPaidEvent.getOrder().getOrderStatus());

        paymentOutboxHelper.save(getUpdatedOrderPaymentOutboxMessage(
                orderPaymentOutboxMessage,
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(orderPaidEvent),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId())
        );

        log.info("Order with id: {} is paid", order.getId().getValue());
    }

    private OrderPaymentOutboxMessage getUpdatedOrderPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id: {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id {} is cancelled", order.getId().getValue());
    }

}
