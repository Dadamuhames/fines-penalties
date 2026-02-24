package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class OtpCheckLockedException extends ApplicationException {
    public OtpCheckLockedException(ErrorCode error) {
        super(error);
    }
}
