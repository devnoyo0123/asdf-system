package com.example.orderservice.adapter.outbox.scheduler.payment;

import com.example.modulecommon.outbox.OutboxScheduler;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.application.ports.output.scheduler.payment.PaymentOutboxHelper;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Component
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper paymentOutboxHelper) {
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> response =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);
        if(response.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = response.get();
            log.debug("Retrieved {} outbox messages for removed. payloads: {}", outboxMessages.size(),
                    outboxMessages.stream().map(OrderPaymentOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);
            log.debug("{} outbox messages removed!", outboxMessages.size());
        }
    }
}
