package com.example.restaurantservice.adapter.web;

import com.example.restaurantservice.application.dto.RestaurantQuery;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import com.example.restaurantservice.application.ports.input.RestaurantApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantApplicationService restaurantApplicationService;

    public RestaurantController(RestaurantApplicationService restaurantApplicationService) {
        this.restaurantApplicationService = restaurantApplicationService;
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<RestaurantQueryResponse> getProducts(
            @PathVariable("id") UUID restaurantId,
            @RequestParam("productId") List<UUID> productIds) {

        RestaurantQueryResponse restaurantQueryResponse =
                restaurantApplicationService.findOneBy(
                        RestaurantQuery.of(restaurantId, productIds));

        log.debug("Returning restaurant with id: {}", restaurantQueryResponse.id());
        return ResponseEntity.ok(restaurantQueryResponse);
    }
}
