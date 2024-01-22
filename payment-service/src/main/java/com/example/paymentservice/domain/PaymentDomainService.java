package com.example.paymentservice.domain;

import com.example.modulecommon.domain.valueobject.PaymentStatus;
import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.domain.entity.Payment;
import com.example.paymentservice.domain.event.PaymentCancelledEvent;
import com.example.paymentservice.domain.event.PaymentCompletedEvent;
import com.example.paymentservice.domain.event.PaymentEvent;
import com.example.paymentservice.domain.event.PaymentFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;


@Slf4j
@Service
public class PaymentDomainService {

    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   List<String> failureMessages
                                                   ) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        } else {
            log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))), failureMessages);
        }
    }

    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 List<String> failureMessages) {
        payment.validatePayment(failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))), failureMessages);
        }
    }

}
