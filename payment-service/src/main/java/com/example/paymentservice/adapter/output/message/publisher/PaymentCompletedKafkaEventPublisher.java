package com.example.paymentservice.adapter.output.message.publisher;

import com.example.modulecommon.kafka.order.avro.model.PaymentResponseAvroModel;
import com.example.modulecommon.kafka.producer.service.KafkaMessageHelper;
import com.example.modulecommon.kafka.producer.service.KafkaProducer;
import com.example.paymentservice.adapter.output.message.mapper.PaymentMessagePublisherMapper;
import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.config.PaymentServiceConfigData;
import com.example.paymentservice.domain.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentCompletedKafkaEventPublisher implements PaymentEventPublisher<PaymentCompletedEvent> {

    private final PaymentMessagePublisherMapper paymentMessagePublisherMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentCompletedKafkaEventPublisher(PaymentMessagePublisherMapper paymentMessagePublisherMapper,
                                               KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                               PaymentServiceConfigData paymentServiceConfigData,
                                               KafkaMessageHelper kafkaMessageHelper) {
        this.paymentMessagePublisherMapper = paymentMessagePublisherMapper;
        this.kafkaProducer = kafkaProducer;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

        log.debug("Received PaymentCompletedEvent for order id: {}", orderId);

//        try {
//            PaymentResponseAvroModel paymentResponseAvroModel =
//                    paymentMessagePublisherMapper.paymentCompletedEventToPaymentResponseAvroModel(domainEvent);
//
//            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
//                    orderId,
//                    paymentResponseAvroModel,
//                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentResponseTopicName(),
//                            paymentResponseAvroModel,
//                            orderId,
//                            paymentResponseAvroModel.getClassSchema().getName()));
//
//            log.debug("PaymentResponseAvroModel sent to kafka for order id: {}", orderId);
//        } catch (Exception e) {
//            log.error("Error while sending PaymentResponseAvroModel message" +
//                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
//        }
    }
}
