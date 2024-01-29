package com.example.restaurantservice.application.ports.input;

import com.example.restaurantservice.application.dto.RestaurantQuery;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;

public interface RestaurantApplicationService {

    RestaurantQueryResponse findOneBy(RestaurantQuery query);
}
