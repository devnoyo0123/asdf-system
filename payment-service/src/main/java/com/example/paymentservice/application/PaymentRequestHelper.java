package com.example.paymentservice.application;

import com.example.modulecommon.domain.valueobject.PaymentStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.paymentservice.application.dto.PaymentRequest;
import com.example.paymentservice.application.mapper.PaymentDataMapper;
import com.example.paymentservice.application.ports.output.dataaccess.repository.PaymentRepository;
import com.example.paymentservice.application.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.example.paymentservice.application.outbox.scheduler.OrderOutboxHelper;
import com.example.paymentservice.domain.PaymentDomainService;
import com.example.paymentservice.domain.entity.Payment;
import com.example.paymentservice.domain.event.PaymentEvent;
import com.example.paymentservice.domain.exception.PaymentNotFoundException;
import com.example.paymentservice.domain.outbox.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                OrderOutboxHelper orderOutboxHelper,
                                PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
    }

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if(publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.debug("outbox message with saga id: {} is already saved to db",
                    paymentRequest.getSagaId());
            return;
        }

        log.debug("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, failureMessages);
        paymentRepository.save(payment);

        orderOutboxHelper.saveOrderOutboxMessage(
                paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId())
                );

    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest ) {
        if(publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.debug("outbox message with saga id: {} is already saved to db",
                    paymentRequest.getSagaId());
            return;
        }

        log.debug("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if(paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentNotFoundException("Payment with order id: {} "+
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Payment payment = paymentResponse.get();
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment,failureMessages);
        paymentRepository.save(payment);

        orderOutboxHelper.saveOrderOutboxMessage(
                paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId())
        );
    }


    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
                                                              PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(
                        UUID.fromString(paymentRequest.getSagaId()),
                        paymentStatus);
        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxMessage);
            return true;
        }
        return false;
    }
}
