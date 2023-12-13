package com.example.modulecommon.domain.event.publisher;

import com.example.modulecommon.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
