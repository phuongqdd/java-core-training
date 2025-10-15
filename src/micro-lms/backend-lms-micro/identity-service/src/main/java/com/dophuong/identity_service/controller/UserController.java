package com.dophuong.identity_service.controller;

import com.dophuong.identity_service.dto.request.PasswordChangeRequest;
import com.dophuong.identity_service.dto.request.UserUpdateProfileRequest;
import com.dophuong.identity_service.dto.response.ApiResponse;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Principal principal){
        String username = principal.getName();
        UserResponse profile = userService.getProfile(username);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy thông tin thành công")
                        .data(profile)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<Long>> getUserId(@PathVariable String username){
        return ResponseEntity.ok(ApiResponse.<Long>builder()
                        .data(userService.getUserIdByUsername(username))
                        .message("Lấy thành công ID")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestBody UserUpdateProfileRequest request,
                                                                   Authentication authentication){
        UserResponse userResponse = userService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                        .status(200)
                        .message("Cập nhật profile thành công")
                        .data(userResponse)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody PasswordChangeRequest request,
            Authentication authentication
            ){
        String username = authentication.getName();
        userService.changePassword(username, request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .status(200)
                        .message("Đổi mật khẩu thành công!")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách user thành công")
                        .data(userService.getAllUsers())
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/test")
    public ResponseEntity<Boolean> test(){
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }

}
