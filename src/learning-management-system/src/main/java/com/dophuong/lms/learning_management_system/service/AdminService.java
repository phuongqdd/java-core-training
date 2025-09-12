package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    void unLockUser(Long id);

    UserResponse createUser(UserCreateRequest request, Role role);

    UserResponse getStudent(Long id);

    List<UserResponse> getAllStudents();

    UserResponse getInstructor(Long id);

    List<UserResponse> getAllInstructors();
}
