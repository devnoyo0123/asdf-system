package com.example.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = {"com.example.restaurantservice", "com.example.modulecommon.dataaccess"}
)
@EntityScan(
        basePackages = {"com.example.restaurantservice", "com.example.modulecommon.dataaccess"}
)
@SpringBootApplication(
        scanBasePackages = {"com.example.restaurantservice", "com.example.modulecommon"}
)
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }

}
