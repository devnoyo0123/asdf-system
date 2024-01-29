package com.example.orderservice.application;

import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.customer.executor.CustomerExecutor;
import com.example.orderservice.application.ports.output.customer.executor.RestaurantExecutor;
import com.example.orderservice.application.ports.output.order.repository.OrderRepository;
import com.example.orderservice.domain.OrderDomainService;
import com.example.modulecommon.domain.entity.Customer;
import com.example.orderservice.domain.entity.Order;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import com.example.orderservice.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerExecutor customerExecutor;
    private final RestaurantExecutor restaurantExecutor;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService, OrderRepository orderRepository, CustomerExecutor customerExecutor, RestaurantExecutor restaurantExecutor, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerExecutor = customerExecutor;
        this.restaurantExecutor = restaurantExecutor;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantExecutor.getRestaurantByIdAndProductIdIn(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: "+ createOrderCommand.getRestaurantId());
        }
        return optionalRestaurant.get();
    }


    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer= customerExecutor.getCustomerBy(customerId);
        if(customer.isEmpty()){
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: "+customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if( orderResult == null) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.debug("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }
}
