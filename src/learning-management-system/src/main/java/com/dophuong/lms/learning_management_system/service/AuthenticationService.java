package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.*;
import com.dophuong.lms.learning_management_system.dto.response.AuthenticationResponse;
import com.dophuong.lms.learning_management_system.dto.response.IntrospectResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.User;
import jakarta.validation.Valid;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    IntrospectResponse introspectResponse(IntrospectRequest request);

    void logout(LogoutRequest request);

    void increaseFailedAttempts(User user);
    void resetFailedAttempts(User user);
    void lockAccount(User user);
    boolean unlockWhenTimeExpired(User user);

    UserResponse signup(@Valid UserCreateRequest request, String role);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
