package com.dophuong.identity_service.service;

import com.dophuong.identity_service.dto.request.*;
import com.dophuong.identity_service.dto.response.AuthenticationResponse;
import com.dophuong.identity_service.dto.response.IntrospectResponse;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.entity.User;
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

    void addGlobalRoleToUser(Long userId, String roleName);
}
