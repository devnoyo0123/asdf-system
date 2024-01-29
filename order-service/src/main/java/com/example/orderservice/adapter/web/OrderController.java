package com.example.orderservice.adapter.web;

import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.dto.track.TrackOrderQuery;
import com.example.orderservice.application.dto.track.TrackOrderResponse;
import com.example.orderservice.application.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.debug("Creating order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(),
                createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.debug("Order created with tracking id: {}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.debug("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingId());
        return  ResponseEntity.ok(trackOrderResponse);
    }
}