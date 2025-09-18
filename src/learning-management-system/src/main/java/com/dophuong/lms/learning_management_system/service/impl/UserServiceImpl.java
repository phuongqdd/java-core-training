package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.PasswordChangeRequest;
import com.dophuong.lms.learning_management_system.dto.request.UserUpdateProfileRequest;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.UserMapper;
import com.dophuong.lms.learning_management_system.repository.UserRepository;
import com.dophuong.lms.learning_management_system.service.UserService;
import com.dophuong.lms.learning_management_system.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserResponse getProfile(String username) {
        User user = getUserByUsername(username);

        log.warn("Hi HI" + user.getUserRoles());
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateProfile(String username, UserUpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        StringUtil.updateIfNotBlank(request.getFullName(), user::setFullName);
        StringUtil.updateIfNotBlank(request.getEmail(), user::setEmail);
        StringUtil.updateIfNotBlank(request.getPhone(), user::setPhone);
        StringUtil.updateIfNotBlank(request.getAvatarUrl(), user::setAvatarUrl);

        if(request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if(request.getGender() != null) user.setGender(request.getGender());

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Override
    public void changePassword(String username, PasswordChangeRequest request) {
        User user = getUserByUsername(username);

        // Kiểm tra mật khẩu hiện tại
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        // Cập nhật mật khẩu mới, không rỗng
        String newPassword = request.getNewPassword();
        if(newPassword == null || newPassword.isBlank()){
            throw new AppException(ErrorCode.NEW_PASSWORD_EMPTY);
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCH);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

}
