package com.dophuong.lms.learning_management_system.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // User
    USER_EXISTED(1001, "User đã tồn tại", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "User không tồn tại", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL(1003, "Email đã tồn tại", HttpStatus.CONFLICT),
    DUPLICATE_PHONE(1004, "Số điện thoại đã tồn tại", HttpStatus.CONFLICT),
    NEW_PASSWORD_EMPTY(1005, "Mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1006, "Mật khẩu hiện tại không đúng", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(1007, "Sai mật khẩu hoặc username", HttpStatus.UNAUTHORIZED),
    USER_LOCKED(1008, "Tài khoản bị khóa", HttpStatus.LOCKED),
    PASSWORD_CONFIRM_NOT_MATCH(1009, "Mật khẩu xác nhận không khớp", HttpStatus.BAD_REQUEST),

    // Role / Permission
    ROLE_NOT_FOUND(1101, "Không tìm thấy role trong hệ thống", HttpStatus.BAD_REQUEST),
    ROlE_EXISTED(1102, "Role đã tồn tại", HttpStatus.CONFLICT),
    PERMISSION_DENIED_ADD_USER(1103, "Chỉ INSTRUCTOR trong khóa học hoặc ADMIN mới được thêm người vào khóa học này", HttpStatus.FORBIDDEN),

    // Instructor
    INSTRUCTOR_NOT_FOUND(1201, "Không tìm thấy instructor", HttpStatus.NOT_FOUND),

    // Student
    STUDENT_NOT_FOUND(1301, "Không tìm thấy student", HttpStatus.NOT_FOUND),

    // Token / Authentication
    UNAUTHENTICATED(1401, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1402, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1403, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALIDATED(1404, "Token đã bị thu hồi", HttpStatus.UNAUTHORIZED),

    //Course
    USER_ALREADY_ENROLLED(1501, "User đã tham gia khóa học", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1502, "Khóa học không tồn tại", HttpStatus.NOT_FOUND),
    QUESTION_MINIMUM_CHOICES_REQUIRED(1503, "Câu hỏi phải có ít nhất 2 lựa chọn", HttpStatus.BAD_REQUEST),

    //Question
    QUESTION_NOT_FOUND(1512, "Câu hỏi không tồn tại", HttpStatus.NOT_FOUND),


    //Option
    OPTION_NOT_FOUND(1601, "Đáp án không tồn tại", HttpStatus.NOT_FOUND)
    ;

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
