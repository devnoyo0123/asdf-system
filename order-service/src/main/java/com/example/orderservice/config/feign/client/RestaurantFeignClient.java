package com.example.orderservice.config.feign.client;

import com.example.orderservice.config.feign.config.FeignConfig;
import com.example.orderservice.config.feign.dto.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "restaurant-service",
        url = "${feign.restaurant-service.url.prefix}",
        configuration = FeignConfig.class
)
public interface RestaurantFeignClient {

    @GetMapping("/api/restaurants/{restaurantId}/products")
    ResponseEntity<RestaurantDTO> callGet(
            @PathVariable("restaurantId") String restaurantId,
            @RequestParam("productId") List<String> productIds);
}
