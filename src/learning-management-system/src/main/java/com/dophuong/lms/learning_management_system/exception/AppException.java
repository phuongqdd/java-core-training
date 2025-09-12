package com.dophuong.lms.learning_management_system.exception;

import com.dophuong.lms.learning_management_system.enums.ErrorCode;

public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
