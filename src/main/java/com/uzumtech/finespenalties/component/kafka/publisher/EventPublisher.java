package com.uzumtech.finespenalties.component.kafka.publisher;

public interface EventPublisher<E> {
    void publish(final E event);
}
