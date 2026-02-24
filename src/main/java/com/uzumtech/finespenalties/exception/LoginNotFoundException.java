package com.uzumtech.finespenalties.exception;


import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginNotFoundException extends ApplicationException {
    public LoginNotFoundException(ErrorCode error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
