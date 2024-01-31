package com.example.orderservice.application.mapper.order;

import com.example.modulecommon.domain.entity.Customer;
import com.example.modulecommon.domain.entity.Product;
import com.example.modulecommon.domain.entity.Restaurant;
import com.example.modulecommon.domain.valueobject.*;
import com.example.modulecommon.kafka.order.avro.model.PaymentOrderStatus;
import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.dto.create.OrderAddressDto;
import com.example.orderservice.application.dto.create.OrderItemDto;
import com.example.orderservice.application.dto.track.TrackOrderResponse;
import com.example.orderservice.config.feign.dto.CustomerDTO;
import com.example.orderservice.config.feign.dto.RestaurantDTO;
import com.example.orderservice.domain.entity.*;
import com.example.orderservice.domain.event.OrderCancelledEvent;
import com.example.orderservice.domain.event.OrderCreatedEvent;
import com.example.orderservice.domain.event.OrderPaidEvent;
import com.example.orderservice.domain.outbox.approval.OrderApprovalEventPayload;
import com.example.orderservice.domain.outbox.approval.OrderApprovalEventProduct;
import com.example.orderservice.domain.outbox.payment.OrderPaymentEventPayload;
import com.example.orderservice.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItemList().stream().map(orderItemDto ->
                                Product.of(ProductId.of(orderItemDto.getProductId())))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(CustomerId.of(createOrderCommand.getCustomerId()))
                .restaurantId(RestaurantId.of(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(Money.of(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItemList()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDto> itemList) {

        return itemList.stream().map(orderItemDto ->
                OrderItem.builder().product(Product.of(ProductId.of(orderItemDto.getProductId())))
                        .price(Money.of(orderItemDto.getPrice()))
                        .quantity(orderItemDto.getQuantity())
                        .subTotal(Money.of(orderItemDto.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddressDto address) {
        return new StreetAddress(UUID.randomUUID(), address.street(), address.postalCode());
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCreatedEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCreatedEvent.getOrder().getId().getValue().toString())
                .price(orderCreatedEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCreatedEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

    public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantApprovalStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream().map(orderItem ->
                        OrderApprovalEventProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(OrderCancelledEvent
                                                                                           orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();

    }

    public Optional<Customer> customerDTOToCustomer(CustomerDTO body) {

        return Optional.of(Customer.of(
                CustomerId.of(UUID.fromString(body.id())),
                body.name(),
                body.phone(),
                body.street())
        );
    }

    public Optional<Restaurant> restaurantDTOToRestaurant(RestaurantDTO body) {
        return Optional.of(
                Restaurant.builder()
                        .restaurantId(RestaurantId.of(UUID.fromString(String.valueOf(body.id()))))
                        .active(body.active())
                        .products(body.products().stream()
                                .map(productDTO -> Product.of(
                                        ProductId.of(UUID.fromString(String.valueOf(productDTO.id()))),
                                        productDTO.name(),
                                        Money.of(productDTO.price())
                                        ))
                                .collect(Collectors.toList())
                        ).build()
        );
    }
}
