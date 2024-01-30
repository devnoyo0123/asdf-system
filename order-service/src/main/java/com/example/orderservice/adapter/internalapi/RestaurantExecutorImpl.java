package com.example.orderservice.adapter.internalapi;

import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.customer.executor.RestaurantExecutor;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.orderservice.config.feign.client.RestaurantFeignClient;
import com.example.orderservice.config.feign.dto.RestaurantDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RestaurantExecutorImpl implements RestaurantExecutor {

    private final RestaurantFeignClient restaurantFeignClient;
    private final OrderDataMapper orderDataMapper;

    public RestaurantExecutorImpl(RestaurantFeignClient restaurantFeignClient, OrderDataMapper orderDataMapper) {
        this.restaurantFeignClient = restaurantFeignClient;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public Optional<Restaurant> getRestaurantByIdAndProductIdIn(Restaurant restaurant) {
        List<String> productIds = restaurant.getProducts().stream()
                .map(product -> String.valueOf(product.getId().getValue())).collect(Collectors.toList());
        ResponseEntity<RestaurantDTO> response = restaurantFeignClient.callGet(String.valueOf(restaurant.getId().getValue()),productIds);
        return orderDataMapper.restaurantDTOToRestaurant(response.getBody());
    }
}
