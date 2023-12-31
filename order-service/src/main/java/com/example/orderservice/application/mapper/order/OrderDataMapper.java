package com.example.orderservice.application.mapper.order;

import com.example.modulecommon.domain.valueobject.CustomerId;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.ProductId;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.orderservice.application.dto.create.CreateOrderCommand;
import com.example.orderservice.application.dto.create.CreateOrderResponse;
import com.example.orderservice.application.dto.create.OrderAddressDto;
import com.example.orderservice.application.dto.create.OrderItemDto;
import com.example.orderservice.application.dto.track.TrackOrderResponse;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.entity.OrderItem;
import com.example.orderservice.domain.entity.Product;
import com.example.orderservice.domain.entity.Restaurant;
import com.example.orderservice.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .productLists(createOrderCommand.getItemList().stream().map(orderItemDto ->
                                new Product(new ProductId(orderItemDto.getProductId())))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItemList()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDto> itemList) {
        return itemList.stream().map(orderItemDto ->
                OrderItem.builder().product(new Product(new ProductId(orderItemDto.getProductId())))
                        .price(new Money(orderItemDto.getPrice()))
                        .quantity(orderItemDto.getQuantity())
                        .subTotal(new Money(orderItemDto.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddressDto address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
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
}
