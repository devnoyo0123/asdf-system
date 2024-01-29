package com.example.restaurantservice.application.mapper;

import com.example.restaurantservice.application.dto.ProductDto;
import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.application.dto.RestaurantQuery;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import com.example.restaurantservice.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class RestaurantDataMapper {

    public RestaurantQueryResponse restaurantToRestaurantQueryResponse(Restaurant restaurant) {
        return new RestaurantQueryResponse(
                restaurant.getId().getValue(),
                restaurant.getProducts().stream()
                        .map(product -> new ProductDto(
                                product.getId().getValue(),
                                product.getName(),
                                product.getPrice().getAmount(),
                                product.isAvailable()
                        ))
                        .toList(),
                restaurant.isActive()
                );
    }
}
