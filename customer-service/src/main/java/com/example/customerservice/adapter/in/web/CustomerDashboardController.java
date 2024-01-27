package com.example.customerservice.adapter.in.web;

import com.example.customerservice.adapter.dto.CustomerDashboardQueryRequest;
import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.mapper.CustomerDashboardMapper;
import com.example.customerservice.application.mapper.CustomerDataMapper;
import com.example.customerservice.application.ports.input.CustomerApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer-dashboard")
public class CustomerDashboardController {

    private final CustomerDashboardMapper customerDashboardMapper;
    private final CustomerApplicationService customerApplicationService;

    public CustomerDashboardController(CustomerDashboardMapper customerDashboardMapper, CustomerApplicationService customerApplicationService) {
        this.customerDashboardMapper = customerDashboardMapper;
        this.customerApplicationService = customerApplicationService;
    }


    @GetMapping("")
    public ResponseEntity<Page<CustomerDashboardQueryResponse>> search(CustomerDashboardQueryRequest customerDashboardQueryRequest) {
        // 로직 구현
        CustomerDashboardQuery customerDashboardQuery = customerDashboardMapper.customerDashboardQueryRequestToCustomerDashboardQuery(customerDashboardQueryRequest);
        Page<CustomerDashboardQueryResponse> response = customerApplicationService.search(customerDashboardQuery);
        return ResponseEntity.ok(response);
    }
}
