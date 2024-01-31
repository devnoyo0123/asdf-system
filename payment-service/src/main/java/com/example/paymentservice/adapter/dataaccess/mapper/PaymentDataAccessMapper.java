package com.example.paymentservice.adapter.dataaccess.mapper;

import com.example.modulecommon.domain.valueobject.CustomerId;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.paymentservice.adapter.dataaccess.entity.PaymentEntity;
import com.example.paymentservice.domain.entity.Payment;
import com.example.paymentservice.domain.valueobject.PaymentId;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {
    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerId().getValue())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentId(PaymentId.of(paymentEntity.getId()))
                .customerId(CustomerId.of(paymentEntity.getCustomerId()))
                .orderId(OrderId.of(paymentEntity.getOrderId()))
                .price(Money.of(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }
}
