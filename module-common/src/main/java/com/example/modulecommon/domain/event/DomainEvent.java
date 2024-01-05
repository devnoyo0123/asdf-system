package com.example.modulecommon.domain.event;

public interface DomainEvent<T>{
    void fire();
}
