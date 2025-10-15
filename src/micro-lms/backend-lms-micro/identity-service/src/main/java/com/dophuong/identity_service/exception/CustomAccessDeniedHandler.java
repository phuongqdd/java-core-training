package com.dophuong.identity_service.exception;

import com.dophuong.identity_service.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    // Sử dụng ObjectMapper và đăng ký module để serialize LocalDateTime
    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // xử lý LocalDateTime
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8"); // thêm dòng này

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .message("Bạn không đủ quyền để thực hiện hành động này!")
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        // Chuyển sang JSON và viết ra response
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }
}
