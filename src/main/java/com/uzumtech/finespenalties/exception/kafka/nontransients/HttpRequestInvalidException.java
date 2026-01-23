package com.uzumtech.finespenalties.exception.kafka.nontransients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;

public class HttpRequestInvalidException extends NonTransientException {
    public HttpRequestInvalidException(ErrorCode error, Exception ex) {
        super(error.getCode(), ex.getMessage(), ErrorType.INTERNAL, null);
    }
}
