package com.example.paymentservice.application.ports.input.message.listener;


import com.example.paymentservice.application.dto.PaymentRequest;

public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
