package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class OffenseNotFoundException extends ApplicationException  {

    public OffenseNotFoundException(ErrorCode error) {
        super(error);
    }

    public OffenseNotFoundException(ErrorCode error, HttpStatus status) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, status);
    }
}
