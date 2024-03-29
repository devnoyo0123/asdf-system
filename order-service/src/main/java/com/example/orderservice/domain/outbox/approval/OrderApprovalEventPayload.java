package com.example.orderservice.domain.outbox.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalEventPayload {
    @JsonProperty
    private String orderId;
    @JsonProperty
    private String restaurantId;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private String restaurantApprovalStatus;
    @JsonProperty
    private List<OrderApprovalEventProduct> products;
}
