package com.dophuong.identity_service.service;

import com.dophuong.identity_service.dto.request.PasswordChangeRequest;
import com.dophuong.identity_service.dto.request.UserUpdateProfileRequest;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    UserResponse getIdInLogin();

    UserResponse getProfile(String username);

    UserResponse updateProfile(String username, UserUpdateProfileRequest request);

    void changePassword(String username, PasswordChangeRequest request);

    List<UserResponse> getAllUsers();

    Long getUserIdByUsername(String username);

    UserResponse getUserByUserName(String username);
}
