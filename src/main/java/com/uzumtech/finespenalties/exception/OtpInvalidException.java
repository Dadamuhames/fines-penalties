package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class OtpInvalidException extends ApplicationException {
    public OtpInvalidException(ErrorCode error) {
        super(error);
    }
}
