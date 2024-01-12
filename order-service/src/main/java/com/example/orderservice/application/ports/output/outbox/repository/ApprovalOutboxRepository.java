package com.example.orderservice.application.ports.output.outbox.repository;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.domain.outbox.approval.OrderApprovalOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalOutboxRepository {
    OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderPaymentOutboxMessage);

    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                      OutboxStatus outboxStatus,
                                                                                      SagaStatus... sagaStatus);
    Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                      UUID sagaId,
                                                                      SagaStatus... sagaStatus);
    void deleteByTypeAndOutboxStatusAndSagaStatus(String type,
                                                  OutboxStatus outboxStatus,
                                                  SagaStatus... sagaStatus);
}
