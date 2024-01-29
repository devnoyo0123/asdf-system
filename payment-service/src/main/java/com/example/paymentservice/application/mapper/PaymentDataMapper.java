package com.example.paymentservice.application.mapper;

import com.example.modulecommon.domain.valueobject.CustomerId;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.paymentservice.application.dto.PaymentRequest;
import com.example.paymentservice.domain.entity.Payment;
import com.example.paymentservice.domain.event.PaymentEvent;
import com.example.paymentservice.domain.outbox.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {

    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(Money.of(paymentRequest.getPrice()))
                .build();
    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent paymentEvent) {
        return OrderEventPayload.builder()
                .paymentId(paymentEvent.getPayment().getId().getValue().toString())
                .customerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
                .orderId(paymentEvent.getPayment().getOrderId().getValue().toString())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentStatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
