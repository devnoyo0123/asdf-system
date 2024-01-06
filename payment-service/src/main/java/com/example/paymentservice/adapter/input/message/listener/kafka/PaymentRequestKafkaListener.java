package com.example.paymentservice.adapter.input.message.listener.kafka;

import com.example.modulecommon.kafka.consumer.service.KafkaConsumer;
import com.example.modulecommon.kafka.order.avro.model.PaymentOrderStatus;
import com.example.modulecommon.kafka.order.avro.model.PaymentRequestAvroModel;
import com.example.paymentservice.adapter.input.message.mapper.PaymentMessageListenerMapper;
import com.example.paymentservice.application.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessageListenerMapper paymentMessageListenerMapper;

    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                       PaymentMessageListenerMapper paymentMessageListenerMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessageListenerMapper = paymentMessageListenerMapper;
    }

    @Override
    @KafkaListener(topics = "${payment-service.payment-request-topic-name}", id = "${kafka-consumer-config.payment-consumer-group-id}")
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY)List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION)List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET)List<Long> offsets) {

        log.info("{} number of payment request received with keys: {}, partitions: {}, offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(paymentRequestAvroModel -> {
            if(PaymentOrderStatus.PENDING == paymentRequestAvroModel.getPaymentOrderStatus()){
                log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.completePayment(paymentMessageListenerMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel));
            } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.getPaymentOrderStatus()) {
                log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.cancelPayment(paymentMessageListenerMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel));
            }
        });
    }
}
