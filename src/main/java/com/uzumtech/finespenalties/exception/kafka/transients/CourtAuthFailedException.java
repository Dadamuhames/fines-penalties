package com.uzumtech.finespenalties.exception.kafka.transients;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class CourtAuthFailedException extends TransientException {
    public CourtAuthFailedException(ErrorCode errorCode, Exception ex) {
        super(errorCode, ex);
    }
}
