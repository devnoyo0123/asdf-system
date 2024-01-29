package com.example.orderservice.adapter.message.listener;

import com.example.modulecommon.kafka.consumer.service.KafkaConsumer;
import com.example.modulecommon.kafka.order.avro.model.PaymentResponseAvroModel;
import com.example.modulecommon.kafka.order.avro.model.PaymentStatus;
import com.example.orderservice.adapter.message.mapper.OrderMessagingDataMapper;
import com.example.orderservice.application.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.example.orderservice.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(groupId= "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.debug("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {
            try {
                if(PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                    log.debug("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                            .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus() ||
                    PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()) {
                    log.debug("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper
                            .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                }
            } catch (OptimisticEntityLockException e) {
                log.error("Caught optimistic lock exception while payment response kafka listener was processing for order id: {}",
                        paymentResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {} while payment response kafka listener was processing",
                        paymentResponseAvroModel.getOrderId());
            }
        });
    }
}
