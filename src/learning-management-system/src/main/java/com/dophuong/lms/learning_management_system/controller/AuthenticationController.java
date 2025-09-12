package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.*;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.AuthenticationResponse;
import com.dophuong.lms.learning_management_system.dto.response.IntrospectResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@Valid @RequestBody UserCreateRequest request){
        UserResponse user = authenticationService.signup(request, Role.STUDENT);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo tài khoản thành công")
                        .data(user)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        AuthenticationResponse result = authenticationService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(result.isAuthenticated() ? 200 : 401)
                .message(result.isAuthenticated() ? "Đăng nhập thành công" : "Đăng nhập thất bại")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response = authenticationService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request){
        IntrospectResponse response = authenticationService.introspectResponse(request);

        return ApiResponse.<IntrospectResponse>builder()
                .status(response.isValid() ? 200 : 401)
                .message(response.isValid() ? "Đăng nhập thành công" : "Đăng nhập thất bại")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request){
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .status(200)
                .message("Đăng xuất đã được xử lý thành công")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
