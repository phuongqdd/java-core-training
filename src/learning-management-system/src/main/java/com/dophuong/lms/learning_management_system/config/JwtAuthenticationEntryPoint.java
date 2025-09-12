package com.dophuong.lms.learning_management_system.config;

import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint
 *
 * Class này được Spring Security gọi tới khi người dùng cố gắng truy cập vào
 * tài nguyên cần xác thực nhưng lại chưa đăng nhập hoặc JWT không hợp lệ.
 * Mục đích: Trả về JSON thông báo lỗi thay vì trả về trang HTML mặc định.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Phương thức commence() được Spring Security gọi khi có lỗi Authentication
     * (chưa đăng nhập, token sai, token hết hạn...)
     *
     * @param request HttpServletRequest - request gốc từ client
     * @param response HttpServletResponse - response để ghi dữ liệu trả về
     * @param authException AuthenticationException - exception chứa thông tin lỗi
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Lấy mã lỗi chuẩn từ enum ErrorCode (ví dụ: 1007 - UNAUTHENTICATED)
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        // Set HTTP status code tương ứng (ví dụ: 401 Unauthorized)
        response.setStatus(errorCode.getHttpStatus().value());

        // Định dạng dữ liệu trả về là JSON + UTF-8 để hiển thị tiếng Việt không lỗi font
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        // Tạo response body theo chuẩn ApiResponse (dùng builder pattern)
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(errorCode.getCode())       // Mã lỗi nghiệp vụ (ví dụ: 1007)
                .message(errorCode.getMessage())   // Thông báo lỗi (ví dụ: "Chưa xác thực")
                .build();

        // Tạo ObjectMapper để chuyển object -> JSON
        // Lưu ý: dùng new ObjectMapper() thì chưa có cấu hình mặc định của Spring,
        // nên ta phải tự cấu hình thêm module hỗ trợ cho LocalDateTime.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // hỗ trợ serialize/deserialize LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // tắt timestamp, thay bằng định dạng ISO (2025-09-11T17:00:00)

        // Ghi chuỗi JSON vào response body trả về cho client
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        // Đảm bảo dữ liệu được flush về client ngay lập tức
        response.flushBuffer();
    }
}

