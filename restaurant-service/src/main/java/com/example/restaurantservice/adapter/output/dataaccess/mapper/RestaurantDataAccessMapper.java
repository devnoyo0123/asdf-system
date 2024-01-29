package com.example.restaurantservice.adapter.output.dataaccess.mapper;


import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.modulecommon.domain.valueobject.ProductId;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.adapter.output.dataaccess.entity.OrderApprovalEntity;
import com.example.restaurantservice.adapter.output.dataaccess.entity.ProductEntity;
import com.example.restaurantservice.adapter.output.dataaccess.entity.RestaurantEntity;
import com.example.restaurantservice.application.dto.ProductDto;
import com.example.restaurantservice.application.dto.RestaurantQueryResponse;
import com.example.restaurantservice.domain.entity.OrderApproval;
import com.example.restaurantservice.domain.entity.Product;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.valueobject.OrderApprovalId;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RestaurantDataAccessMapper {

    public Restaurant restaurantEntityToRestaurant(RestaurantEntity restaurantEntity) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getId()))
                .products(restaurantEntity.getRestaurantProducts().stream()
                        .map(restaurantProduct -> this.productEntityToProduct(restaurantProduct.getProduct()))
                        .toList())
                .isActive(restaurantEntity.isActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(orderApprovalEntity.getId()))
                .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .orderId(new OrderId(orderApprovalEntity.getOrderId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }

    public RestaurantQueryResponse restaurantEntityToRestaurantQueryResponse(RestaurantEntity restaurantEntity) {
        return RestaurantQueryResponse.of(
                restaurantEntity.getId(),
                restaurantEntity.getRestaurantProducts().stream()
                        .map(restaurantProduct -> this.productEntityToProductDto(restaurantProduct.getProduct()))
                        .toList(),
                restaurantEntity.isActive()
        );
    }

    private ProductDto productEntityToProductDto(ProductEntity productEntity) {
        return ProductDto.of(
                productEntity.getId(),
                productEntity.getName(),
                new BigDecimal(String.valueOf(productEntity.getPrice())),
                productEntity.isAvailable()
        );
    }

    private Product productEntityToProduct(ProductEntity productEntity) {
        return Product.builder()
                .productId(new ProductId(productEntity.getId()))
                .name(productEntity.getName())
                .price(Money.of(new BigDecimal(String.valueOf(productEntity.getPrice()))))
                .isAvailable(productEntity.isAvailable())
                .build();

    }
}
