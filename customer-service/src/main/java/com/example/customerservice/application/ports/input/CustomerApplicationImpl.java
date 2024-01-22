package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.CustomerFindOneQueryHandler;
import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerApplicationImpl implements CustomerApplicationService{

    private final CustomerFindOneQueryHandler customerFindOneQueryHandler;

    public CustomerApplicationImpl(CustomerFindOneQueryHandler customerFindOneQueryHandler) {
        this.customerFindOneQueryHandler = customerFindOneQueryHandler;
    }

    @Override
    public CustomerFindOneQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery) {
        return customerFindOneQueryHandler.findOneCustomerBy(findOneCustomerQuery);
    }
}
