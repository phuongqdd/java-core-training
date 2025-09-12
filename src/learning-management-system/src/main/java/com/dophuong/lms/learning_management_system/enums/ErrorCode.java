package com.dophuong.lms.learning_management_system.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_EXISTED(1001, "User đã tồn tại", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "User không tồn tại", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL(1003, "Email đã tồn tại", HttpStatus.CONFLICT),
    DUPLICATE_PHONE(1004, "Số điện thoại đã tồn tại", HttpStatus.CONFLICT),
    INSTRUCTOR_NOT_FOUND(1005, "Không tìm thấy instructor", HttpStatus.NOT_FOUND),
    STUDENT_NOT_FOUND(1005, "Không tìm thấy student", HttpStatus.NOT_FOUND),

    LOGIN_FAILED(1006, "Sai mật khẩu hoặc username", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1007, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1008, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1009, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALIDATED(1010, "Token đã bị thu hồi", HttpStatus.UNAUTHORIZED),
    USER_LOCKED(1011, "Tài khoản bị khóa", HttpStatus.LOCKED);

    private final int code;                  // Mã lỗi nội bộ
    private final String message;            // Thông báo lỗi
    private final HttpStatus httpStatus;     // HTTP status trả về

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
