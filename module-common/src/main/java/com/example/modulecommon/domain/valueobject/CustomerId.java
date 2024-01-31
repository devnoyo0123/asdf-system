package com.example.modulecommon.domain.valueobject;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {
    protected CustomerId(UUID value) {
        super(value);
    }

    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }
}
