package com.example.orderservice.config.feign.client;

import com.example.orderservice.config.feign.config.FeignConfig;
import com.example.orderservice.config.feign.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "customer-service",
        url = "${feign.customer-service.url.prefix}",
        configuration = FeignConfig.class
)
public interface CustomerFeignClient {

    @GetMapping("/api/customers/{id}")
    ResponseEntity<CustomerDTO> callGet(@PathVariable("id") UUID id);

}
