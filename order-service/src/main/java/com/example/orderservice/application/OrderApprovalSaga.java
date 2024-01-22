package com.example.orderservice.application;

import com.example.modulecommon.domain.valueobject.OrderStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.modulecommon.saga.SagaStep;
import com.example.orderservice.application.dto.message.RestaurantApproveResponse;
import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.scheduler.approval.ApprovalOutboxHelper;
import com.example.orderservice.application.ports.output.scheduler.payment.PaymentOutboxHelper;
import com.example.orderservice.domain.OrderDomainService;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import com.example.orderservice.domain.outbox.approval.OrderApprovalOutboxMessage;
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
public class OrderApprovalSaga implements SagaStep<RestaurantApproveResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderDataMapper orderDataMapper;


    public OrderApprovalSaga(OrderDomainService orderDomainService,
                             OrderSagaHelper orderSagaHelper, ApprovalOutboxHelper approvalOutboxHelper, PaymentOutboxHelper paymentOutboxHelper, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.orderDataMapper = orderDataMapper;
    }


    @Override
    @Transactional
    public void process(RestaurantApproveResponse restaurantApproveResponse) {

        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(restaurantApproveResponse.getSagaId()),
                        SagaStatus.PROCESSING
                );

        if( orderApprovalOutboxMessageResponse.isEmpty()) {
            log.info("Outbox message with saga id :{} is already processed!", restaurantApproveResponse.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();

        Order order = approveOrder(restaurantApproveResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                orderApprovalOutboxMessage,
                order.getOrderStatus(),
                sagaStatus)
        );

        paymentOutboxHelper.save(getUpdatedOrderPaymentOutboxMessage(
                restaurantApproveResponse.getSagaId(),
                order.getOrderStatus(),
                sagaStatus)
        );

        log.info("Order with id: {} approved", restaurantApproveResponse.getOrderId());
    }

    @Override
    @Transactional
    public void rollback(RestaurantApproveResponse restaurantApproveResponse) {

        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                    UUID.fromString(restaurantApproveResponse.getSagaId()),
                    SagaStatus.PROCESSING
        );

        if(orderApprovalOutboxMessageResponse.isEmpty()) {
            log.info("Outbox message with saga id :{} is already roll backed!", restaurantApproveResponse.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();

        OrderCancelledEvent domainEvent = rollbackOfOrderApproval(restaurantApproveResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                orderApprovalOutboxMessage,
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus)
        );

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(domainEvent),
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApproveResponse.getSagaId())
        );

        log.info("Order with id: {} cancelled", domainEvent.getOrder().getId().getValue());
    }

    private OrderCancelledEvent rollbackOfOrderApproval(RestaurantApproveResponse restaurantApproveResponse) {
        log.info("Cancelling order with id: {}", restaurantApproveResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApproveResponse.getOrderId());
        OrderCancelledEvent domainEvent = orderDomainService.cancelOrderPayment(order,
                restaurantApproveResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return domainEvent;
    }

    private OrderPaymentOutboxMessage getUpdatedOrderPaymentOutboxMessage(String sagaId, OrderStatus orderStatus, SagaStatus sagaStatus) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(sagaId),
                SagaStatus.PROCESSING
        );

        if(orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("Outbox message with saga id :{} is already processed!", sagaId);
            return null;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

    private Order approveOrder(RestaurantApproveResponse restaurantApproveResponse) {
        log.info("Approving order with id: {}", restaurantApproveResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApproveResponse.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        return order;
    }
}
