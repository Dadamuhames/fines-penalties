package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class WebhookAccessDeniedException extends ApplicationException {
    public WebhookAccessDeniedException(ErrorCode error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.FORBIDDEN);
    }
}
