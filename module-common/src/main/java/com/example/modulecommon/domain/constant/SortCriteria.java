package com.example.modulecommon.domain.constant;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortCriteria {
    @NotNull private String key;
    @NotNull @Builder.Default private OrderSort orderSort = OrderSort.DEFAULT;

    @Builder
    public SortCriteria(String key, OrderSort orderSort) {
        this.key = key;
        this.orderSort = orderSort;
    }
}