package com.example.orderservice.adapter.out.message.publisher.kafka;

import com.example.modulecommon.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.example.modulecommon.kafka.producer.service.KafkaMessageHelper;
import com.example.modulecommon.kafka.producer.service.KafkaProducer;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.orderservice.adapter.out.message.mapper.OrderMessagingDataMapper;
import com.example.orderservice.application.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.example.orderservice.config.OrderServiceConfigData;
import com.example.orderservice.domain.outbox.approval.OrderApprovalEventPayload;
import com.example.orderservice.domain.outbox.approval.OrderApprovalOutboxMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovalEventKafkaPublisher(
            OrderMessagingDataMapper orderMessagingDataMapper,
            KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
            OrderServiceConfigData orderServiceConfigData,
            KafkaMessageHelper kafkaMessageHelper
         ) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage, BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
        OrderApprovalEventPayload orderApprovalEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderApprovalOutboxMessage.getPayload(),
                        OrderApprovalEventPayload.class);
        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

        log.debug("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
                orderApprovalEventPayload.getOrderId(),
                sagaId);

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel =
                    orderMessagingDataMapper.orderApprovalEventToRestaurantApprovalRequestAvroModel(
                            sagaId,
                            orderApprovalEventPayload);

            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            sagaId,
                    restaurantApprovalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderApprovalOutboxMessage,
                            outboxCallback,
                            orderApprovalEventPayload.getOrderId(),
                            restaurantApprovalRequestAvroModel.getSchema().getName()
                    )
            );

            log.debug("OrderApprovalEventPayload sent to kafka for order id: {} and saga id: {}",
                    restaurantApprovalRequestAvroModel.getOrderId(),sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovalEventPayload to kafka for order id: {} and saga id: {}," +
                    " error: {}", orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
        }

    }
}
