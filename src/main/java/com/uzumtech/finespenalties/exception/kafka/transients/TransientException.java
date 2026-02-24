package com.uzumtech.finespenalties.exception.kafka.transients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import com.uzumtech.finespenalties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TransientException extends ApplicationException {

    public TransientException(Exception ex) {
        super(ErrorCode.INTERNAL_SERVICE_ERROR_CODE.getCode(), ex.getMessage(), ErrorType.INTERNAL, null);
    }


    public TransientException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public TransientException(ErrorCode errorCode, Exception ex) {
        super(errorCode.getCode(), errorCode.getMessage(), ErrorType.INTERNAL, null);
    }

}
