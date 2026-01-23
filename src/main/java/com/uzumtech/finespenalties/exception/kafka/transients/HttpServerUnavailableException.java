package com.uzumtech.finespenalties.exception.kafka.transients;

public class HttpServerUnavailableException extends TransientException {
    public HttpServerUnavailableException(Exception ex) {
        super(ex);
    }
}
