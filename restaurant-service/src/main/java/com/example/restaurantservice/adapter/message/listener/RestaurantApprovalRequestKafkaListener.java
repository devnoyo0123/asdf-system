package com.example.restaurantservice.adapter.message.listener;

import com.example.modulecommon.kafka.consumer.service.KafkaConsumer;
import com.example.modulecommon.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.example.restaurantservice.adapter.message.mapper.RestaurantMessagingDataMapper;
import com.example.restaurantservice.application.exception.RestaurantApplicationServiceException;
import com.example.restaurantservice.application.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.example.restaurantservice.domain.exception.RestaurantNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

    public RestaurantApprovalRequestKafkaListener(RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener, RestaurantMessagingDataMapper restaurantMessagingDataMapper) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
    }

    @Override
    @KafkaListener(topics = "${restaurant-service.restaurant-approval-request-topic-name}",
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}")
    public void receive(
            @Payload List<RestaurantApprovalRequestAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.debug("{} number of orders approval requests received with keys:{}, partitions:{} and offsets: {}" +
                " , sending for restaurant approval",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(restaurantApprovalRequestAvroModel -> {
            try {
                log.debug("Processing order approval event at: {}", System.nanoTime());
                restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper
                        .restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel));
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    log.error("Caught unique constraint exception with sql state: {} " +
                                    "in RestaurantApprovalRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), restaurantApprovalRequestAvroModel.getOrderId());
                } else {
                    throw new RestaurantApplicationServiceException("Throwing DataAccessException in" +
                            " RestaurantApprovalRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (RestaurantNotFoundException e) {
                log.error("No restaurant found for restaurant id: {}, and order id: {}",
                        restaurantApprovalRequestAvroModel.getRestaurantId(),
                        restaurantApprovalRequestAvroModel.getOrderId());
            }
        });
    }
}
