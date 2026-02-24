package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class NotificationIdInvalidException extends ApplicationException {
    public NotificationIdInvalidException(ErrorCode error) {
        super(error);
    }
}
