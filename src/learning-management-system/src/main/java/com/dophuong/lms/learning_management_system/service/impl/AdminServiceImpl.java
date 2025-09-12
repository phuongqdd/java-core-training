package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.UserMapper;
import com.dophuong.lms.learning_management_system.repository.UserRepository;
import com.dophuong.lms.learning_management_system.service.AdminService;
import com.dophuong.lms.learning_management_system.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void unLockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setLockTime(null);

        userRepository.save(user);
    }

    @Override
    public UserResponse createUser(UserCreateRequest request, Role role) {
        return authenticationService.signup(request, role);
    }

    @Override
    public UserResponse getInstructor(Long id) {
        User user = userRepository.findByIdAndRole(id, Role.INSTRUCTOR)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllInstructors() {
        List<User> users= userRepository.findAllByRole(Role.INSTRUCTOR);
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getStudent(Long id) {
        User user = userRepository.findByIdAndRole(id, Role.STUDENT)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllStudents() {
        List<User> users = userRepository.findAllByRole(Role.STUDENT);
        return userMapper.toResponseList(users);
    }
}
