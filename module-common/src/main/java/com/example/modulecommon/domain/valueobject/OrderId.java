package com.example.modulecommon.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID>{
    protected OrderId(UUID value) {
        super(value);
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }
}
