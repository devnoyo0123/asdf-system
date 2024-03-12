package com.example.orderservice.domain;

import com.example.modulecommon.domain.entity.Product;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.modulecommon.domain.valueobject.CustomerId;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.ProductId;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.entity.OrderItem;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import com.example.orderservice.domain.exception.InvalidProductRequestException;
import com.example.orderservice.domain.exception.OrderDomainException;
import com.example.orderservice.domain.valueobject.StreetAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDomainServiceImplTest {

    private final OrderDomainService orderDomainService;

    OrderDomainServiceImplTest() {
        this.orderDomainService = new OrderDomainServiceImpl();
    }


    @DisplayName("주문생성 성공 테스트")
    @Test
    void validateAndInitiateOrder() {

        // Given
        String CUSTOMER_ID = "7f1f98b5-5103-48ca-81c9-60ee3d133211";
        String RESTAURANT_ID = "97829a7a-ebde-444d-9016-e35c26f90e13";
        String PRODUCT_ID = "716326a2-95aa-4573-bb62-f4ee7d541f0e";


        Order order = Order.builder()
                .customerId(CustomerId.of(UUID.fromString(CUSTOMER_ID)))
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .deliveryAddress(new StreetAddress(UUID.randomUUID(), "여의대로 38", "45812"))
                .price(Money.of(BigDecimal.valueOf(10000)))
                .items(List.of(OrderItem.builder()
                        .product(Product.of(ProductId.of(UUID.fromString(PRODUCT_ID))))
                        .price(Money.of(BigDecimal.valueOf(10000)))
                        .quantity(1)
                        .subTotal(Money.of(BigDecimal.valueOf(10000)))
                        .build()))
                .build();

        Product product = Product.of(
                    ProductId.of(UUID.fromString(PRODUCT_ID)), "product1", Money.of(BigDecimal.valueOf(10000))
                );

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .products(List.of(product))
                .active(true)
                .build();

        // When
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderCreated = orderCreatedEvent.getOrder();

        RestaurantId restaurantId = orderCreatedEvent.getOrder().getRestaurantId();

        // Then
        assertEquals(orderCreated, order);
        assertEquals(restaurantId, RestaurantId.of(UUID.fromString(RESTAURANT_ID)));
    }

    @DisplayName("주문결제 실패 테스트 - 주문상품이 없을 때")
    @Test
    void validateAndInitiateOrder_fail() {
        // Given
        String CUSTOMER_ID = "7f1f98b5-5103-48ca-81c9-60ee3d133211";
        String RESTAURANT_ID = "97829a7a-ebde-444d-9016-e35c26f90e13";
        String PRODUCT_ID = "716326a2-95aa-4573-bb62-f4ee7d541f0e";


        Order order = Order.builder()
                .customerId(CustomerId.of(UUID.fromString(CUSTOMER_ID)))
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .deliveryAddress(new StreetAddress(UUID.randomUUID(), "여의대로 38", "45812"))
                .price(Money.of(BigDecimal.valueOf(10000)))
                .items(List.of(OrderItem.builder()
                        .product(Product.of(ProductId.of(UUID.fromString(PRODUCT_ID))))
                        .price(Money.of(BigDecimal.valueOf(10000)))
                        .quantity(1)
                        .subTotal(Money.of(BigDecimal.valueOf(10000)))
                        .build()))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .active(true)
                .build();

        // When & Then: assertThrows를 사용하여 예외 발생 검증
        assertThrows(InvalidProductRequestException.class, () -> {
            orderDomainService.validateAndInitiateOrder(order, restaurant);
        });
    }

    @DisplayName("주문결제 실패 테스트 - Restaurant가 비활성화 상태일 때")
    @Test
    void validateAndInitiateOrder_fail2() {
        // Given
        String CUSTOMER_ID = "7f1f98b5-5103-48ca-81c9-60ee3d133211";
        String RESTAURANT_ID = "97829a7a-ebde-444d-9016-e35c26f90e13";
        String PRODUCT_ID = "716326a2-95aa-4573-bb62-f4ee7d541f0e";


        Order order = Order.builder()
                .customerId(CustomerId.of(UUID.fromString(CUSTOMER_ID)))
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .deliveryAddress(new StreetAddress(UUID.randomUUID(), "여의대로 38", "45812"))
                .price(Money.of(BigDecimal.valueOf(10000)))
                .items(List.of(OrderItem.builder()
                        .product(Product.of(ProductId.of(UUID.fromString(PRODUCT_ID))))
                        .price(Money.of(BigDecimal.valueOf(10000)))
                        .quantity(1)
                        .subTotal(Money.of(BigDecimal.valueOf(10000)))
                        .build()))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .active(false)
                .build();

        // When & Then: assertThrows를 사용하여 예외 발생 검증
        OrderDomainException exception = assertThrows(OrderDomainException.class, () -> {
            orderDomainService.validateAndInitiateOrder(order, restaurant);
        });

        // Then
        assertEquals("Restaurant with id " + restaurant.getId().getValue() +
                " is currently not active!", exception.getMessage());
    }

    @DisplayName("주문결제 실패 테스트 - 주문금액이 0원일 때")
    @Test
    void validateAndInitiateOrder_fail3() {
        // Given
        String CUSTOMER_ID = "7f1f98b5-5103-48ca-81c9-60ee3d133211";
        String RESTAURANT_ID = "97829a7a-ebde-444d-9016-e35c26f90e13";
        String PRODUCT_ID = "716326a2-95aa-4573-bb62-f4ee7d541f0e";


        Order order = Order.builder()
                .customerId(CustomerId.of(UUID.fromString(CUSTOMER_ID)))
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .deliveryAddress(new StreetAddress(UUID.randomUUID(), "여의대로 38", "45812"))
                .items(List.of(OrderItem.builder()
                        .product(Product.of(ProductId.of(UUID.fromString(PRODUCT_ID))))
                        .price(Money.of(BigDecimal.valueOf(10000)))
                        .quantity(1)
                        .subTotal(Money.of(BigDecimal.valueOf(10000)))
                        .build()))
                .build();

        Product product = Product.of(
                ProductId.of(UUID.fromString(PRODUCT_ID)), "product1", Money.of(BigDecimal.valueOf(10000))
        );

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(RestaurantId.of(UUID.fromString(RESTAURANT_ID)))
                .products(List.of(product))
                .active(true)
                .build();

        // When & Then: assertThrows를 사용하여 예외 발생 검증
        OrderDomainException exception = assertThrows(OrderDomainException.class, () -> {
            orderDomainService.validateAndInitiateOrder(order, restaurant);
        });

        // Then
        assertEquals("Total price must be greater than zero!", exception.getMessage());
    }
}