package com.example.orderservice.domain;

import com.example.orderservice.domain.entity.Order;
import com.example.modulecommon.domain.entity.Product;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import com.example.orderservice.domain.event.OrderPaidEvent;
import com.example.orderservice.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderDomainServiceImpl implements OrderDomainService{

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                      Restaurant restaurant
                                                      ) {
        validateRestaurant(restaurant);
        validateOrderProductPriceWithRestaurant(order, restaurant);
        order.validateOrder();
        order.initOrder();
        log.debug("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateOrderProductPriceWithRestaurant(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> {
            Product orderProduct = orderItem.getProduct();
            Optional<Product> matchingRestaurantProduct = restaurant.getProducts().stream()
                    .filter(restaurantProduct -> restaurantProduct.equals(orderProduct))
                    .findAny();

            if (matchingRestaurantProduct.isEmpty() || !matchingRestaurantProduct.get().getPrice().equals(orderProduct.getPrice())) {
                throw new OrderDomainException("가격 정보가 올바르지 않습니다. 제품 ID: " + orderProduct.getId().getValue());
            }
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.debug("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.debug("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order,
                                                  List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.debug("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.debug("Order with id: {} is cancelled", order.getId().getValue());
    }
}
