package com.uzumtech.finespenalties.dto.response.error;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import com.uzumtech.finespenalties.exception.ApplicationException;

import java.util.List;


public record ErrorResponse(int code,
                            String message,
                            ErrorType type,
                            List<String> validationErrors) {


    public static ErrorResponse of(ErrorCode error, String message, ErrorType type) {
        return new ErrorResponse(error.getCode(), message, type, null);
    }

    public static ErrorResponse of(int code, String message, ErrorType type) {
        return new ErrorResponse(code, message, type, null);
    }

    public static ErrorResponse of(ApplicationException ex) {
        return new ErrorResponse(ex.getCode(), ex.getMessage(), ex.getErrorType(), null);
    }

    public static ErrorResponse of(int code, String message, ErrorType type, List<String> error) {
        return new ErrorResponse(code, message, type, error);
    }
}
