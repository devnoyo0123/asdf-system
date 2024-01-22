package com.example.restaurantservice.application;

import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.application.mapper.RestaurantDataMapper;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.example.restaurantservice.application.ports.output.repository.OrderApprovalRepository;
import com.example.restaurantservice.application.ports.output.repository.RestaurantRepository;
import com.example.restaurantservice.domain.RestaurantDomainService;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.event.OrderApprovalEvent;
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
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        var orderApprovalEvent = restaurantDomainService.validateOrder(
                restaurant,
                failureMessages);
        orderApprovalRepository.persist(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.
                restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantResult = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with restaurant id: " + restaurant.getId().getValue() +
                    " not found!");
        }

        Restaurant restaurantEntity = restaurantResult.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> {
            restaurantEntity.getOrderDetail().getProducts().forEach(productEntity -> {
                if (productEntity.getId().equals(product.getId())) {
                    product.updateWithConfirmedNameAndPriceAndAvailability(productEntity.getName(),
                            productEntity.getPrice(),
                            productEntity.getQuantity(),
                            productEntity.isAvailable());

                }
            });
        });
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
        return restaurant;
    }
}
