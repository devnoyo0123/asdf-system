package com.example.orderservice.adapter.internalapi;

import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.output.customer.executor.CustomerExecutor;
import com.example.orderservice.config.feign.client.CustomerFeignClient;
import com.example.orderservice.config.feign.dto.CustomerDTO;
import com.example.modulecommon.domain.entity.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
public class CustomerExecutorImpl implements CustomerExecutor {

    private final CustomerFeignClient customerFeignClient;
    private final OrderDataMapper orderDataMapper;

    public CustomerExecutorImpl(CustomerFeignClient customerFeignClient, OrderDataMapper orderDataMapper) {
        this.customerFeignClient = customerFeignClient;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public Optional<Customer> getCustomerBy(UUID id) {

        ResponseEntity<CustomerDTO> response = customerFeignClient.callGet(id);
        return orderDataMapper.customerDTOToCustomer(response.getBody());
    }
}
