package com.example.modulecommon.web.dto;

import lombok.Getter;

@Getter
public abstract class PageRequestDTO {
    private final int page;
    private final int size;

    protected PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }
}