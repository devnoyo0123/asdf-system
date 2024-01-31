package com.example.orderservice.adapter.outbox.scheduler.approval;

import com.example.modulecommon.outbox.OutboxScheduler;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.application.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.example.orderservice.application.ports.output.scheduler.approval.ApprovalOutboxHelper;
import com.example.orderservice.domain.outbox.approval.OrderApprovalOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    public RestaurantApprovalOutboxScheduler(ApprovalOutboxHelper approvalOutboxHelper, RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher) {
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.restaurantApprovalRequestMessagePublisher = restaurantApprovalRequestMessagePublisher;
    }

    @Override
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessageResponse = approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                OutboxStatus.STARTED,
                SagaStatus.PROCESSING);

        if(outboxMessageResponse.isPresent() && !outboxMessageResponse.get().isEmpty()) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessageResponse.get();
            log.debug("Retrieved {} outbox messages with ids: {}, sending to message bus",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            outboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));

            outboxMessages.forEach(outboxMessage ->
                    restaurantApprovalRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));

            log.debug("{} OrderApprovalOutboxMessage sent to message bus", outboxMessages.size());
        }
    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(outboxMessage);
        log.debug("OrderApprovalOutboxMessage is updated with id: {}, outboxStatus: {}",
                outboxMessage.getId().toString(),
                outboxStatus.toString());

    }
}
