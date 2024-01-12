package com.example.orderservice.application.ports.output.scheduler.payment;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.adapter.out.scheduler.exception.PaymentOutboxSchedulerException;
import com.example.orderservice.application.ports.output.outbox.repository.PaymentOutboxRepository;
import com.example.orderservice.domain.exception.OrderDomainException;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.modulecommon.saga.order.SagaConstants.ORDER_SAGA_NAME;

@Slf4j
@Component
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository paymentOutboxRepository;

    public PaymentOutboxHelper(PaymentOutboxRepository paymentOutboxRepository) {
        this.paymentOutboxRepository = paymentOutboxRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID sagaId,
                                                                                            SagaStatus... sagaStatus) {
        return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME,
                sagaId,
                sagaStatus);
    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage message = paymentOutboxRepository.save(orderPaymentOutboxMessage);
        if (message == null) {
            log.error("Could not save outbox message for order with id: {}", orderPaymentOutboxMessage.getOrderId());
            throw new PaymentOutboxSchedulerException("Could not save outbox message for order with id: " + orderPaymentOutboxMessage.getOrderId());

        }
        log.info("OrderPaymentMessage saved with id: {}", message.getId());
    }
}
