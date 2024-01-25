package com.example.customerservice.adapter.in.web;

import com.example.customerservice.adapter.dto.CustomerDashboardQueryRequest;
import com.example.customerservice.application.dto.*;
import com.example.customerservice.application.mapper.CustomerDataMapper;
import com.example.customerservice.application.ports.input.CustomerApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<CustomerFindOneQueryResponse> customerBy(@PathVariable("id") UUID id) {
        // 로직 구현
        CustomerFindOneQueryResponse customerFindOneQueryResponse = customerApplicationService.findOneCustomerBy(FindOneCustomerQuery.of(id));
        log.debug("Returning customer with id: {}", customerFindOneQueryResponse.id());
        return ResponseEntity.ok(customerFindOneQueryResponse);
    }

}
