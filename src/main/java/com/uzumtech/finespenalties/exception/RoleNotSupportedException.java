package com.uzumtech.finespenalties.exception;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class RoleNotSupportedException extends ApplicationException {
  public RoleNotSupportedException(ErrorCode error) {
    super(error.getCode(), error.getMessage(), ErrorType.EXTERNAL, HttpStatus.BAD_REQUEST);
  }
}
