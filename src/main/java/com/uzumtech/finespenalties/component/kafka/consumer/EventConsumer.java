package com.uzumtech.finespenalties.component.kafka.consumer;

public interface EventConsumer<E> {
    void listen(final E event);

    void dltHandler(E event, String exceptionMessage);
}
