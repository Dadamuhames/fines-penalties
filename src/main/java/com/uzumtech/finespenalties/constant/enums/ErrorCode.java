package com.uzumtech.finespenalties.constant.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVICE_ERROR_CODE(10001, "System not available"),
    EXTERNAL_SERVICE_FAILED_ERROR_CODE(10002, "External service not available"),
    HANDLER_NOT_FOUND_ERROR_CODE(10003, "Handler not found"),
    JSON_NOT_VALID_ERROR_CODE(10004, "Json not valid"),
    VALIDATION_ERROR_CODE(10005, "Validation error"),
    INVALID_REQUEST_PARAM_ERROR_CODE(10006, "Invalid request param"),
    INTERNAL_TIMEOUT_ERROR_CODE(10007, "Internal timeout"),
    METHOD_NOT_SUPPORTED_ERROR_CODE(10008, "Method not supported"),
    MISSING_REQUEST_HEADER_ERROR_CODE(10009, "Missing request header"),

    ROLE_NOT_SUPPORTED_CODE(10010, "Provided Role not supported by the system"),

    INSPECTOR_INVALID_CODE(10011, "Inspector authentication invalid"),
    USER_INVALID_CODE(10012, "User authentication invalid"),
    JWT_INVALID_CODE(10013, "JWT invalid"),
    REFRESH_TOKEN_INVALID_CODE(10014, "Refresh token invalid"),
    LOGIN_INVALID_CODE(10015, "Login invalid"),
    PASSWORD_INVALID_CODE(10016, "Password invalid"),
    OTP_EXPIRED_CODE(10017, "OTP expired or invalid"),

    OTP_REQUEST_LOCKED_CODE(10018, "OTP request locked. Wait and try again"),
    OTP_CHECK_LOCKED_CODE(10019, "Too many incorrect attempts. Wait and try again"),


    HTTP_CLIENT_ERROR_CODE(14000, "Http Client error code"),
    HTTP_SERVER_ERROR_CODE(15000, "Http Server error code"),
    NOTIFICATION_REQUEST_INVALID_CODE(16000, "Request to notification service invalid"),

    LOGIN_OR_TAX_NUMBER_NOT_UNIQUE(12000, "login or taxNumber already in use"),

    EVENT_PUBLISHER_NOT_IMPLEMENTED(13000, "Event publisher not implemented");

    final int code;
    final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
