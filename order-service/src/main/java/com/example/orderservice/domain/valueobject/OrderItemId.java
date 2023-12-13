package com.example.orderservice.domain.valueobject;

import com.example.modulecommon.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
