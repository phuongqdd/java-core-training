package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.PasswordChangeRequest;
import com.dophuong.lms.learning_management_system.dto.request.UserUpdateProfileRequest;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    User getUserByUsername(String username);

    UserResponse getProfile(String username);

    UserResponse updateProfile(String username, UserUpdateProfileRequest request);

    void changePassword(String username, PasswordChangeRequest request);

    List<UserResponse> getAllUsers();
}
