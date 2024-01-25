package com.example.modulecommon.domain.constant;

import lombok.Getter;
import lombok.Setter;

public enum OrderSort {
    ASC("ASC"), // 오름차순
    DESC("DESC"), // 내림차순
    DEFAULT("DESC"); // 기본 값 (기본값을 DESC로 설정)

    @Setter @Getter private final String value;

    OrderSort(String value) {
        this.value = value;
    }
}