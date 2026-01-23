package com.uzumtech.finespenalties.service.intr;

public interface ConsumerService<E> {
    void listen(final E event);

    void dltHandler(E event, String exceptionMessage);
}
