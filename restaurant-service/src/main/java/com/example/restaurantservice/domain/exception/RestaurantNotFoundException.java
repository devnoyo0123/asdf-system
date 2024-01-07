package com.example.restaurantservice.domain.exception;

public class RestaurantNotFoundException extends RestaurantDomainException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
