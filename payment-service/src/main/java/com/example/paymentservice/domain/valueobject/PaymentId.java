package com.example.paymentservice.domain.valueobject;


import com.example.modulecommon.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    protected PaymentId(UUID value) {
        super(value);
    }

    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }
}
