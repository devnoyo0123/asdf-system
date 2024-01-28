package com.example.customerservice.adapter.in.web;

import com.example.customerservice.application.dto.*;
import com.example.customerservice.application.ports.input.CustomerApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    public CustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerQueryResponse> customerBy(@PathVariable("id") UUID id) {
        // 로직 구현
        CustomerQueryResponse customerQueryResponse = customerApplicationService.findOneCustomerBy(FindOneCustomerQuery.of(id));
        log.debug("Returning customer with id: {}", customerQueryResponse.id());
        return ResponseEntity.ok(customerQueryResponse);
    }

}
