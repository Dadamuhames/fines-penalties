package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class OtpRequestLocked extends ApplicationException {
    public OtpRequestLocked(ErrorCode error) {
        super(error);
    }
}
