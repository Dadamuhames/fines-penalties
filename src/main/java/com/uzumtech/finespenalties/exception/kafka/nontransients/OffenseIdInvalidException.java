package com.uzumtech.finespenalties.exception.kafka.nontransients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;

public class OffenseIdInvalidException extends NonTransientException {
    public OffenseIdInvalidException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage(), ErrorType.VALIDATION, null);
    }
}
