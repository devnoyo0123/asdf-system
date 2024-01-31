package com.example.restaurantservice.application.mapper;

import com.example.modulecommon.domain.valueobject.*;
import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.domain.entity.OrderApproval;
import com.example.restaurantservice.domain.entity.OrderDetail;
import com.example.restaurantservice.domain.entity.Product;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.valueobject.OrderApprovalId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderApprovalDataMapper {
    public OrderApproval restaurantApprovalRequestToOrderApproval(Restaurant restaurant, RestaurantApprovalRequest restaurantApprovalRequest) {

        // 제품 ID를 키로 하고 제품 객체를 값으로 하는 맵 생성
        Map<ProductId, Product> productMap = restaurant.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return OrderApproval.builder()
                .orderApprovalId(OrderApprovalId.of(UUID.randomUUID()))
                .orderId(OrderId.of(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                .restaurantId(RestaurantId.of(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(
                        OrderDetail.builder()
                                .products(restaurantApprovalRequest.getProducts().stream().map(
                                                product -> Product.builder()
                                                        .productId(product.getId())
                                                        .price(productMap.get(product.getId()).getPrice())
                                                        .name(productMap.get(product.getId()).getName())
                                                        .isAvailable(productMap.get(product.getId()).isAvailable())
                                                        .quantity(product.getQuantity())
                                                        .build())
                                        .collect(Collectors.toList()))
                                .orderId(OrderId.of(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                                .totalAmount(Money.of(restaurantApprovalRequest.getPrice()))
                                .orderStatus(OrderStatus.valueOf(String.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus())))
                                .build()
                )
                .build();
    }
}
