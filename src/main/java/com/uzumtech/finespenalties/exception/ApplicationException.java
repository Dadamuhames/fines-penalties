package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationException extends RuntimeException {

    int code;
    String message;
    HttpStatus status;
    ErrorType errorType;

    public ApplicationException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
        this.message = message;
        this.errorType = errorType;
    }

    public ApplicationException(ErrorCode error) {
        this(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
