package com.example.orderservice.adapter.out.dataaccess.outbox.payment;

import com.example.modulecommon.domain.valueobject.OrderStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.adapter.dataaccess.outbox.payment.PaymentOutboxRepositoryImpl;
import com.example.orderservice.adapter.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import com.example.orderservice.adapter.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.example.orderservice.application.ports.output.outbox.repository.PaymentOutboxRepository;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("inmemory")
class PaymentOutboxRepositoryImplTest {

    private PaymentOutboxRepository paymentOutboxRepository;
    private PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Autowired
    private EntityManager entityManager; // 테스트 데이터를 위한 EntityManager

    @BeforeEach
    void setUp() {
        paymentOutboxDataAccessMapper = new PaymentOutboxDataAccessMapper();
        paymentOutboxRepository = new PaymentOutboxRepositoryImpl(paymentOutboxJpaRepository, paymentOutboxDataAccessMapper);
    }

    @DisplayName("findByTypeAndSagaIdAndSagaStatus 조회 테스트")
    @Test
    void findByTypeAndSagaIdAndSagaStatus() {

        // 1. 테스트 데이터 준비
        OrderPaymentOutboxMessage testOutboxMessage = OrderPaymentOutboxMessage
                .builder()
                .id(UUID.randomUUID()) // 고유 ID
                .sagaId(UUID.randomUUID()) // 사가 ID
                .createdAt(ZonedDateTime.now()) // 생성된 시간
                .processedAt(ZonedDateTime.now()) // 처리된 시간
                .type("TEST")
                .payload("{\"test\": \"payload\"}") // 페이로드
                .sagaStatus(SagaStatus.STARTED) // 사가 상태
                .orderStatus(OrderStatus.PENDING) // 주문 상태
                .outboxStatus(OutboxStatus.STARTED) // 아웃박스 상태
                .version(0) // 버전
                .build();
        // 여기에 testOutboxMessage를 설정하는 코드 필요
        entityManager.persist(paymentOutboxDataAccessMapper.orderPaymentOutboxMessageToOutboxEntity(testOutboxMessage));

        // 2. 메소드 호출 및 검증
        Optional<OrderPaymentOutboxMessage> result = paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
                testOutboxMessage.getType(),
                testOutboxMessage.getSagaId(),
                testOutboxMessage.getSagaStatus());

        assertTrue(result.isPresent());
    }
}