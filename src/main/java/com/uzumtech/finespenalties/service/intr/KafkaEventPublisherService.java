package com.uzumtech.finespenalties.service.intr;

public interface KafkaEventPublisherService<E> {
    void publish(final E event);
}
