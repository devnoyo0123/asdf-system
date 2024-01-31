package com.example.modulecommon.domain.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID> {
    protected ProductId(UUID value) {
        super(value);
    }

    public static ProductId of(UUID id) {
        return new ProductId(id);
    }
}
