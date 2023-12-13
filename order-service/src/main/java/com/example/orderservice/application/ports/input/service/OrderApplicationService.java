package com.example.orderservice.application.ports.input.service;

import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.dto.track.TrackOrderQuery;
import com.example.orderservice.application.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
