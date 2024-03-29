package com.example.restaurantservice.application;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.restaurantservice.adapter.outbox.scheduler.OrderOutboxHelper;
import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.application.mapper.OrderApprovalDataMapper;
import com.example.restaurantservice.application.mapper.RestaurantDataMapper;
import com.example.restaurantservice.application.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.example.restaurantservice.application.ports.output.repository.OrderApprovalRepository;
import com.example.restaurantservice.application.ports.output.repository.RestaurantRepository;
import com.example.restaurantservice.domain.RestaurantDomainService;
import com.example.restaurantservice.domain.entity.OrderApproval;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.entity.outbox.OrderOutboxMessage;
import com.example.restaurantservice.domain.exception.RestaurantNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final OrderApprovalDataMapper orderApprovalDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           OrderApprovalDataMapper orderApprovalDataMapper, RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository, OrderOutboxHelper orderOutboxHelper, RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.orderApprovalDataMapper = orderApprovalDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.restaurantApprovalResponseMessagePublisher = restaurantApprovalResponseMessagePublisher;
    }

    @Transactional
    public void persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {

        if (publishIfOutboxMessageProcessed(restaurantApprovalRequest)) {
            log.debug("An outbox message with saga id: {} already saved to database!",
                    restaurantApprovalRequest.getSagaId());
            return;
        }

        log.debug("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApproval orderApproval = this.orderApprovalDataMapper.restaurantApprovalRequestToOrderApproval(restaurant, restaurantApprovalRequest);
        var orderApprovalEvent = restaurantDomainService.validateOrder(
                restaurant,
                orderApproval,
                failureMessages);
        orderApprovalRepository.persist(orderApproval);

        orderOutboxHelper
                .saveOrderOutboxMessage(restaurantDataMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                        orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                        OutboxStatus.STARTED,
                        UUID.fromString(restaurantApprovalRequest.getSagaId()));
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {

        Optional<Restaurant> restaurantResult = restaurantRepository.findOneBy(
                UUID.fromString(restaurantApprovalRequest.getRestaurantId()),
                restaurantApprovalRequest.getProducts().stream().map(product -> product.getId().getValue()).toList());

        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurantApprovalRequest.getRestaurantId() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with restaurant id: " + restaurantApprovalRequest.getRestaurantId() +
                    " not found!");
        }

        return restaurantResult.get();
    }

    private boolean publishIfOutboxMessageProcessed(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID
                        .fromString(restaurantApprovalRequest.getSagaId()), OutboxStatus.COMPLETED);
        if (orderOutboxMessage.isPresent()) {
            restaurantApprovalResponseMessagePublisher.publish(orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }
}
