package com.example.paymentservice.application.outbox.scheduler;

import com.example.modulecommon.outbox.OutboxScheduler;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.paymentservice.domain.outbox.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.debug("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());
            orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
            log.debug("Deleted {} OrderOutboxMessage!", outboxMessages.size());
        }
    }
}
