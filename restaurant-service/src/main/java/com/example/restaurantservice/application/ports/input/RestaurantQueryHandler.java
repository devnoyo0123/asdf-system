package com.example.restaurantservice.application.ports.input;

import com.example.restaurantservice.application.dto.RestaurantQuery;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import com.example.restaurantservice.application.mapper.RestaurantDataMapper;
import com.example.restaurantservice.application.ports.output.repository.RestaurantRepository;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.exception.RestaurantNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RestaurantQueryHandler {

    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;

    public RestaurantQueryHandler(RestaurantDataMapper restaurantDataMapper, RestaurantRepository restaurantRepository) {
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public RestaurantQueryResponse findOneBy(RestaurantQuery query) {
        Optional<Restaurant> restaurantResult =  restaurantRepository.findOneBy(
                query.id(),
                query.productIds()
        );
        if(restaurantResult.isEmpty()) {
            throw new RestaurantNotFoundException("Could not find restaurant with id: "+query.id());
        }
        return restaurantDataMapper.restaurantToRestaurantQueryResponse(restaurantResult.get());
    }
}
