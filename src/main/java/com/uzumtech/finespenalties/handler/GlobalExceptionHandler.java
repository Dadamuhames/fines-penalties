package com.uzumtech.finespenalties.handler;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.ErrorType;
import com.uzumtech.finespenalties.dto.response.error.ErrorResponse;
import com.uzumtech.finespenalties.exception.ApplicationException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    private ResponseEntity<ErrorResponse> handleApplicationException(final ApplicationException ex) {
        log.error("ErrorCode: {}", ex.getMessage());

        var errorResponse = ErrorResponse.of(ex);

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    private ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(final MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.MISSING_REQUEST_HEADER_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.HANDLER_NOT_FOUND_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.METHOD_NOT_SUPPORTED_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceAccessException.class)
    private ResponseEntity<ErrorResponse> handleResourceAccessException(final ResourceAccessException ex) {
        log.error("ResourceAccessException = {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.INTERNAL_TIMEOUT_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException = {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.INVALID_REQUEST_PARAM_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage(), ex);

        var validationErrors = ex.getBindingResult().getAllErrors().stream().map(error -> error instanceof FieldError fieldError ? fieldError.getField() + ": " + fieldError.getDefaultMessage() : error.getDefaultMessage()).toList();

        var error = ErrorResponse.of(ErrorCode.VALIDATION_ERROR_CODE.getCode(), ErrorCode.VALIDATION_ERROR_CODE.getMessage(), ErrorType.VALIDATION, validationErrors);

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.JSON_NOT_VALID_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.VALIDATION_ERROR_CODE, ex.getMessage(), ErrorType.VALIDATION);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ErrorResponse> handleNoHandlerFoundException(final NoHandlerFoundException ex) {
        log.error("NoHandlerFoundException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.HANDLER_NOT_FOUND_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<ErrorResponse> handleHttpClientErrorException(final HttpClientErrorException ex) {
        log.error("HttpClientErrorException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.EXTERNAL_SERVICE_FAILED_ERROR_CODE, ex.getMessage(), ErrorType.EXTERNAL);

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    private ResponseEntity<ErrorResponse> handleHttpServerErrorException(final HttpServerErrorException ex) {
        log.error("HttpServerErrorException : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.EXTERNAL_SERVICE_FAILED_ERROR_CODE, ex.getMessage(), ErrorType.EXTERNAL);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {
        log.error("Exception : {}", ex.getMessage(), ex);

        var error = ErrorResponse.of(ErrorCode.INTERNAL_SERVICE_ERROR_CODE, ex.getMessage(), ErrorType.INTERNAL);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
