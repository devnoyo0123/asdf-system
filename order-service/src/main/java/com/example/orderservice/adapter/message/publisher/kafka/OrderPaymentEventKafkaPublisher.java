package com.example.orderservice.adapter.message.publisher.kafka;

import com.example.modulecommon.kafka.order.avro.model.PaymentRequestAvroModel;
import com.example.modulecommon.kafka.producer.service.KafkaMessageHelper;
import com.example.modulecommon.kafka.producer.service.KafkaProducer;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.orderservice.adapter.message.mapper.OrderMessagingDataMapper;
import com.example.orderservice.application.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.example.orderservice.config.OrderServiceConfigData;
import com.example.orderservice.domain.outbox.payment.OrderPaymentEventPayload;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderPaymentEventKafkaPublisher(OrderMessagingDataMapper orderMessagingDataMapper, KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer, OrderServiceConfigData orderServiceConfigData, KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        OrderPaymentEventPayload orderPaymentEventPayload =
                kafkaMessageHelper.getOrderEventPayload(
                        orderPaymentOutboxMessage.getPayload(),
                        OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.debug("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.getOrderId(),
                sagaId);

        PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

        kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                sagaId,
                paymentRequestAvroModel,
                kafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                        paymentRequestAvroModel,
                        orderPaymentOutboxMessage,
                        outboxCallback,
                        orderPaymentEventPayload.getOrderId(),
                        PaymentRequestAvroModel.getClassSchema().getName()));

    }


}
