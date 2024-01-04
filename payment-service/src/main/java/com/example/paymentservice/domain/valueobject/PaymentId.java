package com.example.paymentservice.domain.valueobject;


import com.example.modulecommon.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID value) {
        super(value);
    }
}
