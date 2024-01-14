package com.example.orderservice.application;

import com.example.orderservice.application.dto.message.PaymentResponse;
import com.example.orderservice.application.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.example.orderservice.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSaga.process(paymentResponse);
        log.info("Order Payment saga process is completed for order with id: {}", paymentResponse.getOrderId());
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is rollback for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(),
                paymentResponse.getFailureMessages());
    }
}
