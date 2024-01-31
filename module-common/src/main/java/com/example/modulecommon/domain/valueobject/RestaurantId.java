package com.example.modulecommon.domain.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {
    protected RestaurantId(UUID value) {
        super(value);
    }

    public static RestaurantId of(UUID id) {
        return new RestaurantId(id);
    }
}
