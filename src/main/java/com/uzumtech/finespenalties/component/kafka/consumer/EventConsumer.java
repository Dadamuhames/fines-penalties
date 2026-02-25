package com.uzumtech.finespenalties.component.kafka.consumer;

public interface EventConsumer<E> {
    void listen(E event);

    void dltHandler(E event, String exceptionMessage);
}
