package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;

public class CodeArticleIdInvalidException extends ApplicationException {
    public CodeArticleIdInvalidException(ErrorCode error) {
        super(error);
    }
}
