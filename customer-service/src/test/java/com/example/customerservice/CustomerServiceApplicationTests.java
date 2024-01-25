package com.example.customerservice;

import com.example.customerservice.adapter.out.dataaccess.entity.CustomerEntity;
import com.example.customerservice.adapter.out.dataaccess.repository.CustomerJpaRepository;
import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import com.example.customerservice.application.ports.input.CustomerApplicationService;
import com.example.customerservice.domain.exception.CustomerNotFoundException;
import com.example.modulecommon.domain.valueobject.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("inmemory")
class CustomerServiceApplicationTests {

    @Autowired
    private CustomerApplicationService sut;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @Test
    @DisplayName("when customer found then return customer")
    public void testCustomerBy() {
        // given
        UUID customerId = UUID.fromString("4a8a2752-d82a-42b1-8380-8a7dea2031d2");
        CustomerEntity customerEntity = new CustomerEntity(
                customerId,
                "John Doe",
                "1234567890",
                Address.of(
                "John Doe", "123 Main Street", "12345"
        ));
        customerJpaRepository.save(customerEntity);

        // when
        FindOneCustomerQuery query = FindOneCustomerQuery.of(customerId);
        CustomerFindOneQueryResponse response = sut.findOneCustomerBy(query);

        // then
        assertNotNull(response);
        assertEquals(customerId, response.id());
    }

    @Test
    @DisplayName("when customer not found then throw CustomerNotFoundException")
    public void testCustomerNotFoundException() {
        // given
        UUID nonExistentCustomerId = UUID.randomUUID();

        // when & then
        FindOneCustomerQuery query = FindOneCustomerQuery.of(nonExistentCustomerId);
        assertThrows(CustomerNotFoundException.class, () -> {
            sut.findOneCustomerBy(query);
        });
    }

}
