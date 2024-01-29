package com.example.restaurantservice.application.ports.input;

import com.example.restaurantservice.application.dto.RestaurantQuery;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RestaurantApplicationServiceImpl implements RestaurantApplicationService{

    private final RestaurantQueryHandler restaurantQueryHandler;

    public RestaurantApplicationServiceImpl(RestaurantQueryHandler restaurantQueryHandler) {
        this.restaurantQueryHandler = restaurantQueryHandler;
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantQueryResponse findOneBy(RestaurantQuery query) {
        return restaurantQueryHandler.findOneBy(query);
    }
}
