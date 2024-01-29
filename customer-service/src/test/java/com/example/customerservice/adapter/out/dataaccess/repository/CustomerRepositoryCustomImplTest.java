package com.example.customerservice.adapter.out.dataaccess.repository;

import com.example.customerservice.adapter.dataaccess.entity.CustomerEntity;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import com.example.customerservice.application.ports.output.repository.CustomerRepository;
import com.example.modulecommon.domain.constant.OrderSort;
import com.example.modulecommon.domain.constant.SortCriteria;
import com.example.modulecommon.domain.valueobject.Address;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("inmemory")
@Transactional
class CustomerCustomRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void setUp() {
        // Create 35 customer records with UUIDs
        for (int i = 1; i <= 35; i++) {
            UUID customerId = UUID.randomUUID(); // Generate a random UUID
            CustomerEntity customer = CustomerEntity.builder()
                    .id(customerId)
                    .name("Customer " + i)
                    .address(Address.of(
                            "Street " + i,
                            "12345",
                            "Details " + i)
                    ).build();
            entityManager.persist(customer);
        }
    }

    @DisplayName("기본 검색테스트")
    @Test
    public void testSearchPage(){
        // Test paging with a page size of 10
        Page<CustomerDashboardQueryResponse> page1 = repository.search(
                new CustomerSearchCondition(
                        "",
                        "",
                        SortCriteria.builder()
                                .key("name")
                        .build()), Pageable.ofSize(10).withPage(0));
        Page<CustomerDashboardQueryResponse> page2 = repository.search(CustomerSearchCondition.of(
                "",
                "",
                SortCriteria.builder()
                        .key("street")
                        .build()), Pageable.ofSize(10).withPage(1));
        Page<CustomerDashboardQueryResponse> page3 = repository.search(CustomerSearchCondition.of(
                "",
                "",
                SortCriteria.builder()
                        .key("name")
                        .build()), Pageable.ofSize(10).withPage(2));

        // Assertions
        assertEquals(35, page1.getTotalElements());
        assertEquals(10, page1.getNumberOfElements());
        assertEquals(4, page1.getTotalPages());
        assertEquals("Customer 9", page1.getContent().get(0).name());

        assertEquals(35, page2.getTotalElements());
        assertEquals(10, page2.getNumberOfElements());
        assertEquals(4, page2.getTotalPages());
        assertEquals("Street 31", page2.getContent().get(0).street());

        assertEquals(35, page3.getTotalElements());
        assertEquals(10, page3.getNumberOfElements());
        assertEquals(4, page3.getTotalPages());
        assertEquals("Customer 22", page3.getContent().get(0).name());
    }


    @DisplayName("이름으로 오름차순 정렬 검색 테스트")
    @Test
    public void testSearchPageByNameAsc() {
        CustomerSearchCondition condition = CustomerSearchCondition.of(
                "", "", SortCriteria.builder()
                        .key("name")
                        .orderSort(OrderSort.valueOf("ASC"))
                        .build());

        Page<CustomerDashboardQueryResponse> page = repository.search(
                condition, Pageable.ofSize(10).withPage(0));

        // Assertions for name ascending order
        assertEquals(35, page.getTotalElements());
        assertEquals(10, page.getNumberOfElements());
        assertEquals(4, page.getTotalPages());
        assertTrue(page.getContent().get(0).name().startsWith("Customer 1"));
    }

    @DisplayName("이름으로 내림차순 정렬 검색 테스트")
    @Test
    public void testSearchPageByNameDesc() {
        CustomerSearchCondition condition = CustomerSearchCondition.of(
                "", "", SortCriteria.builder()
                                .key("name")
                                .orderSort(OrderSort.valueOf("DESC"))
                        .build());

        Page<CustomerDashboardQueryResponse> page = repository.search(
                condition, Pageable.ofSize(10).withPage(0));

        // Assertions for name descending order
        assertEquals(35, page.getTotalElements());
        assertEquals(10, page.getNumberOfElements());
        assertEquals(4, page.getTotalPages());
        assertTrue(page.getContent().get(0).name().startsWith("Customer 9"));
    }

    @DisplayName("주소로 오름차순 정렬 검색 테스트")
    @Test
    public void testSearchPageByStreetAsc() {
        CustomerSearchCondition condition = CustomerSearchCondition.of(
                "", "", SortCriteria.builder()
                        .key("street")
                        .orderSort(OrderSort.valueOf("ASC"))
                        .build());

        Page<CustomerDashboardQueryResponse> page = repository.search(
                condition, Pageable.ofSize(10).withPage(1));

        // Assertions for street ascending order
        assertEquals(35, page.getTotalElements());
        assertEquals(10, page.getNumberOfElements());
        assertEquals(4, page.getTotalPages());
        assertTrue(page.getContent().get(0).street().startsWith("Street 19"));
    }

}