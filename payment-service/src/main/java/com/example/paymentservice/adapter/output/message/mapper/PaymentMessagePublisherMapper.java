package com.example.paymentservice.adapter.output.message.mapper;

import com.example.modulecommon.kafka.order.avro.model.PaymentResponseAvroModel;
import com.example.modulecommon.kafka.order.avro.model.PaymentStatus;
import com.example.paymentservice.domain.event.PaymentCancelledEvent;
import com.example.paymentservice.domain.event.PaymentCompletedEvent;
import com.example.paymentservice.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagePublisherMapper {
    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent paymentCompletedEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCompletedEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCompletedEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentCompletedEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentCompletedEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCompletedEvent.getPayment().getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCompletedEvent.getPayment().getPaymentStatus().name()))
                .setFailureMessages(paymentCompletedEvent.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCancelledEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCancelledEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentCancelledEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentCancelledEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCancelledEvent.getPayment().getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.getPayment().getPaymentStatus().name()))
                .setFailureMessages(paymentCancelledEvent.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent paymentFailedEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentFailedEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentFailedEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentFailedEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentFailedEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentFailedEvent.getPayment().getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.getPayment().getPaymentStatus().name()))
                .setFailureMessages(paymentFailedEvent.getFailureMessages())
                .build();
    }
}
