package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.*;
import com.dophuong.lms.learning_management_system.dto.response.AuthenticationResponse;
import com.dophuong.lms.learning_management_system.dto.response.IntrospectResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.InvalidatedToken;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.UserMapper;
import com.dophuong.lms.learning_management_system.repository.InvalidatedTokenRepository;
import com.dophuong.lms.learning_management_system.repository.UserRepository;
import com.dophuong.lms.learning_management_system.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final long EXPIRATION = 3600000;  //thời gian sống
    public static final int MAX_FAILED_ATTEMPTS = 5;        // số lần đăng nhập sai
    public static final long LOCK_TIME_DURATION = 30 * 60 * 1000; // 30 phút (ms)
    private static final long REFRESH_EXPIRATION = 24 * 60 * 60 * 1000;
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvalidatedTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserResponse signup(UserCreateRequest request, Role role) {
        // Chuẩn hóa phone
        String phone = request.getPhone();
        if (phone != null && phone.trim().isEmpty()) {
            phone = null;
        }

        // Check trùng username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Check trùng email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }

        // Check trùng phone
        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE);
        }

        // Map từ DTO sang entity
        User user = userMapper.toEntity(request);

        // Gán role mặc định
        if (role == Role.INSTRUCTOR) {
            user.setRole(Role.INSTRUCTOR);
        } else {
            user.setRole(Role.STUDENT); // mặc định
        }

        // Set các field mặc định quan trọng
        user.setPhone(phone);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        // Mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lưu user
        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // 1. Kiểm tra token có hợp lệ không
        SignedJWT signedJWT;
        try {
            signedJWT = verifyToken(refreshToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Lấy username từ refresh token
        String username;
        try {
            username = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // 3. Tìm user trong DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 4. Sinh access token mới (refresh token giữ nguyên)
        String newAccessToken = generateToken(user, LOCK_TIME_DURATION);

        // 5. Trả response
        return AuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        log.warn("user name: " + request.getUsername());
        log.warn("Mat khau: " + request.getPassword());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Nếu tài khoản bị khóa
        if (!user.getAccountNonLocked()) {
            if (unlockWhenTimeExpired(user)) {
                log.info("Tài khoản {} đã hết thời gian khóa, mở lại", user.getUsername());
            } else {
                throw new AppException(ErrorCode.USER_LOCKED);
            }
        }

        // Kiểm tra mật khẩu
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            increaseFailedAttempts(user);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Reset failed attempts
        resetFailedAttempts(user);

        // Cập nhật thời gian đăng nhập cuối cùng
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Sinh token
        String accessToken = generateToken(user, LOCK_TIME_DURATION);

        String refreshToken = generateToken(user, REFRESH_EXPIRATION);
        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    @Override
    public IntrospectResponse introspectResponse(IntrospectRequest request) {
        String token = request.getToken();
        boolean isValidated = true;

        try {
            verifyToken(token);
        } catch (AppException | JOSEException | ParseException e) {
            log.warn("Token không hợp lệ: {}", e.getMessage());
            isValidated = false;
        }

        return IntrospectResponse.builder()
                .valid(isValidated)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            // Kiểm tra token và lấy SignedJWT
            SignedJWT signToken = verifyToken(request.getToken());

            // Lấy JWT ID (jti) và thời gian hết hạn
            String jti = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // Tạo record token đã bị thu hồi
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            // Lưu vào repository
            tokenRepository.save(invalidatedToken);

            log.info("Token đã được thu hồi thành công: {}", jti);

        } catch (AppException | JOSEException | ParseException e) {
            // Token không hợp lệ hoặc lỗi parse
            log.warn("Token không hợp lệ hoặc lỗi khi logout: {}", e.getMessage());
            // Không ném ra ngoài, logout idempotent
        }
    }

    @Override
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempts);
        userRepository.save(user);

        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            lockAccount(user);
        }
    }

    @Override
    public void resetFailedAttempts(User user) {
        user.setFailedAttempt(0);
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        userRepository.save(user);
    }

    @Override
    public void lockAccount(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(User user) {
        if (user.getLockTime() == null) {
            return true; // chưa bị khóa
        }

        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }

        return false; // vẫn đang bị khóa
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token); // Parse token

        // Kiểm tra signature
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        boolean verified = signedJWT.verify(jwsVerifier);
        log.info("Token guửi: " + signedJWT);
        log.info("Token sinh: " + jwsVerifier);

        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Kiểm tra thời hạn token
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiryTime == null || expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        // Log JWT ID đúng cú pháp SLF4J
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        log.warn("Mã duy nhất cho token: {}", jti);

        // Kiểm tra token đã bị thu hồi
        if (jti != null && tokenRepository.existsById(jti)) {
            throw new AppException(ErrorCode.TOKEN_INVALIDATED);
        }

        return signedJWT;
    }


    private String generateToken(User user, long expiry) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("dophuong.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expiry))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(claims.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        if (user.getRole() != null) {
            return "ROLE_" + user.getRole().name();
        }
        return "";
    }
}
