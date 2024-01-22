package com.example.customerservice.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping("/{id}")
    public String customerBy(@PathVariable("id") Long id) {
        // 로직 구현
        return "customer"; // 실제 구현시 여기에 로직을 추가
    }
}