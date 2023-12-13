package com.example.orderservice.application.ports.input.service.message.listener.payment;

import com.example.orderservice.application.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
