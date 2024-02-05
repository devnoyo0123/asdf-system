package com.example.paymentservice.application.outbox.scheduler;

import com.example.modulecommon.domain.valueobject.PaymentStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.paymentservice.application.ports.output.dataaccess.repository.OrderOutboxRepository;
import com.example.paymentservice.domain.exception.PaymentDomainException;
import com.example.paymentservice.domain.outbox.OrderEventPayload;
import com.example.paymentservice.domain.outbox.OrderOutboxMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.modulecommon.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.time.ZoneOffset.UTC;

@Slf4j
@Component
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    public OrderOutboxHelper(OrderOutboxRepository orderOutboxRepository, ObjectMapper objectMapper) {
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID sagaId,
                                                                                               PaymentStatus
                                                                                                       paymentStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME, sagaId,
                paymentStatus, OutboxStatus.COMPLETED);
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                       PaymentStatus paymentStatus,
                                       OutboxStatus outboxStatus,
                                       UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))))
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .paymentStatus(paymentStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void updateOutboxMessage(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderOutboxMessage);
        log.debug("Order outbox table status is updated as: {}", outboxStatus.name());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new PaymentDomainException("Could not create OrderEventPayload json!", e);
        }
    }

    private void save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutboxMessage!");
            throw new PaymentDomainException("Could not save OrderOutboxMessage!");
        }
        log.debug("OrderOutboxMessage is saved with id: {}", orderOutboxMessage.getId());
    }
}
