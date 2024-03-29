package com.example.orderservice.application;

import com.example.modulecommon.domain.entity.Customer;
import com.example.modulecommon.domain.entity.Product;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.modulecommon.domain.valueobject.*;
import com.example.modulecommon.kafka.order.avro.model.PaymentOrderStatus;
import com.example.modulecommon.outbox.OutboxStatus;
import com.example.modulecommon.saga.SagaStatus;
import com.example.orderservice.OrderServiceApplication;
import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.dto.create.OrderAddressDto;
import com.example.orderservice.application.dto.create.OrderItemDto;
import com.example.orderservice.application.mapper.order.OrderDataMapper;
import com.example.orderservice.application.ports.input.service.OrderApplicationService;
import com.example.orderservice.application.ports.output.customer.executor.CustomerExecutor;
import com.example.orderservice.application.ports.output.customer.executor.RestaurantExecutor;
import com.example.orderservice.application.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.example.orderservice.application.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.example.orderservice.application.ports.output.order.repository.OrderRepository;
import com.example.orderservice.application.ports.output.outbox.repository.PaymentOutboxRepository;
import com.example.orderservice.config.IntegrationTest;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.exception.OrderDomainException;
import com.example.orderservice.domain.outbox.payment.OrderPaymentEventPayload;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.modulecommon.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderServiceApplication.class)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(
        properties = {
                "feign.restaurant-service.url.prefix=http://localhost:${wiremock.server.port}",
                "feign.customer-service.url.prefix=http://localhost:${wiremock.server.port}"
        })
public class OrderApplicationServiceTest extends IntegrationTest {

    @Autowired
    WireMockServer feignMockServer;

    @MockBean
    RestaurantExecutor restaurantExecutor;

    @MockBean
    CustomerExecutor customerExecutor;

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private PaymentOutboxRepository paymentOutboxRepository;

    @MockBean
    public RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    @MockBean
    public PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("60e1b9ba-9191-4d58-89d9-0c4d07859d1f");
    private final UUID RESTAURANT_ID = UUID.fromString("ebcf1720-358b-4a9f-87d0-3d02a10dd8f6");
    private final UUID PRODUCT_ID = UUID.fromString("8aef234c-0304-4324-b6a3-728e4b522c93");
    private final UUID PRODUCT2_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48");
    private final UUID ORDER_ID = UUID.fromString("b3272631-26fa-4691-a10f-f7dec54368b8");
    private final UUID SAGA_ID = UUID.fromString("d31ff6db-52e2-42ac-b845-b49ac678db96");
    private final BigDecimal PRICE = new BigDecimal("26000");

    @BeforeAll
    public void init() {

        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(new OrderAddressDto("여의대로 38", "12345"))
                .price(PRICE)
                .itemList(List.of(OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("5000"))
                                .subTotal(new BigDecimal("5000"))
                        .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT2_ID)
                                .quantity(3)
                                .price(new BigDecimal("7000"))
                                .subTotal(new BigDecimal("21000"))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(new OrderAddressDto("여의대로 38","12345"))
                .price(new BigDecimal("25000"))
                .itemList(List.of(OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("5000"))
                                .subTotal(new BigDecimal("5000"))
                                .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("5000"))
                                .subTotal(new BigDecimal("15000"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(new OrderAddressDto("여의대로 38", "12345"))
                .price(new BigDecimal("21000"))
                .itemList(List.of(OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("6000"))
                                .subTotal(new BigDecimal("6000"))
                                .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("5000"))
                                .subTotal(new BigDecimal("15000"))
                                .build()))
                .build();



    }

    @BeforeAll
    void setUp() {
        this.setupCustomerResponse(feignMockServer);
        this.setupRestaurantResponse(feignMockServer);
    }

    private void setupCustomerResponse(WireMockServer feignMockServer) {
        feignMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/api/restaurants/" + RESTAURANT_ID + "/products?productId=" + PRODUCT_ID+ "&productId=" + PRODUCT_ID))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("payload/restaurant.json")));
    }

    private void setupRestaurantResponse(WireMockServer feignMockServer) {
        feignMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/api/customers/" + CUSTOMER_ID))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("payload/customer.json")));
    }

    @DisplayName("주문이 성공적으로 생성됩니다.")
    @Test
    public void testCreateOrder() {
        // given
        Customer customer = new Customer(CustomerId.of(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .products(List.of(Product.of(ProductId.of(PRODUCT_ID), "product-1", Money.of(new BigDecimal("5000"))),
                        Product.of(ProductId.of(PRODUCT2_ID), "product-2", Money.of(new BigDecimal("7000")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(OrderId.of(ORDER_ID));

        when(customerExecutor.getCustomerBy(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantExecutor.getRestaurantByIdAndProductIdIn(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());

        // when
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);


        // then
        assertEquals(OrderStatus.PENDING,createOrderResponse.getOrderStatus());
        assertEquals("Order created successfully",createOrderResponse.getMessage() );
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @DisplayName("total price가 틀린 주문은 Exception이 발생합니다.")
    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        // given
        Customer customer = new Customer(CustomerId.of(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .products(List.of(Product.of(ProductId.of(PRODUCT_ID), "product-1", Money.of(new BigDecimal("5000"))),
                        Product.of(ProductId.of(PRODUCT_ID), "product-2", Money.of(new BigDecimal("5000")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(OrderId.of(ORDER_ID));

        when(customerExecutor.getCustomerBy(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantExecutor.getRestaurantByIdAndProductIdIn(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());


        // when
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

        // then
        assertEquals("Total price: " + "25000"
                + " is not equal to orderItems total: "+"20000!", orderDomainException.getMessage());
    }

    @DisplayName("product price가 틀린 주문은 Exception이 발생합니다.")
    @Test
    public void testCreateOrderWithWrongProductPrice() {

        // given
        Customer customer = new Customer(CustomerId.of(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .products(List.of(Product.of(ProductId.of(PRODUCT_ID), "product-1", Money.of(new BigDecimal("5000"))),
                        Product.of(ProductId.of(PRODUCT_ID), "product-2", Money.of(new BigDecimal("5000")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(OrderId.of(ORDER_ID));

        when(customerExecutor.getCustomerBy(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantExecutor.getRestaurantByIdAndProductIdIn(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());

        // when
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));

        // then
        assertEquals("Order item price: 6000 is not valid for product " + PRODUCT_ID, orderDomainException.getMessage());
    }

    @DisplayName("주문을 생성할 때, 식당이 오픈한 상태가 아니면 Exception이 발생합니다.")
    @Test
    public void testCreateOrderWithPassiveRestaurant() {

        // given
        Customer customer = new Customer(CustomerId.of(CUSTOMER_ID));
        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .products(List.of(Product.of(ProductId.of(PRODUCT_ID), "product-1", Money.of(new BigDecimal("5000"))),
                        Product.of(ProductId.of(PRODUCT_ID), "product-2", Money.of(new BigDecimal("5000")))))
                .active(false)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(OrderId.of(ORDER_ID));

        when(customerExecutor.getCustomerBy(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantExecutor.getRestaurantByIdAndProductIdIn(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());

        // when
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));

        // then
        assertEquals("Restaurant with id " + RESTAURANT_ID + " is currently not active!", orderDomainException.getMessage());
    }

    private OrderPaymentOutboxMessage getOrderPaymentOutboxMessage() {
        OrderPaymentEventPayload orderPaymentEventPayload = OrderPaymentEventPayload.builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(ZonedDateTime.now())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();

        return OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(OrderStatus.PENDING)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }
}
