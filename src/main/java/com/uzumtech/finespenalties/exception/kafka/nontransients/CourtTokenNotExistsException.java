package com.uzumtech.finespenalties.exception.kafka.nontransients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;

public class CourtTokenNotExistsException extends NonTransientException {
    public CourtTokenNotExistsException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage(), ErrorType.INTERNAL, null);
    }
}
