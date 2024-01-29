package com.example.customerservice.adapter.web.dto;

import com.example.modulecommon.web.dto.PageRequestDTO;
import lombok.Getter;

@Getter
public class CustomerDashboardQueryRequest extends PageRequestDTO {
    private final String name;
    private final String street;
    private final String detail;
    private final String sortKey;
    private final String sortDirection;

    public CustomerDashboardQueryRequest(String name, String street, String detail, int page, int size, String sortKey, String sortDirection) {
        super(page, size);
        this.name = name;
        this.street = street;
        this.detail = detail;
        this.sortKey = sortKey;
        this.sortDirection = sortDirection;
    }
}
