package com.example.paymentservice.adapter.dataaccess.repository;

import com.example.modulecommon.domain.valueobject.PaymentStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.paymentservice.adapter.dataaccess.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {

    Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxEntity> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                    UUID sagaId,
                                                                    PaymentStatus paymentStatus,
                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}
