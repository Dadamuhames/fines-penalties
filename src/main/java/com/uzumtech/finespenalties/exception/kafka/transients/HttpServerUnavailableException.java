package com.uzumtech.finespenalties.exception.kafka.transients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class HttpServerUnavailableException extends TransientException {
    public HttpServerUnavailableException(Exception ex) {
        super(ex);
    }


    public HttpServerUnavailableException(ErrorCode errorCode, Exception ex) {
        super(errorCode, ex);
    }
}
