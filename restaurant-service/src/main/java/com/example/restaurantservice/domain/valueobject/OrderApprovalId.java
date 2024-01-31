package com.example.restaurantservice.domain.valueobject;

import com.example.modulecommon.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    protected OrderApprovalId(UUID value) {
        super(value);
    }

    public static OrderApprovalId of(UUID id) {
        return new OrderApprovalId(id);
    }
}
