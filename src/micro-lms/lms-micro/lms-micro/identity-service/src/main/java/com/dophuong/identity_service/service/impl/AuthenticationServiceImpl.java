package com.dophuong.identity_service.service.impl;

import com.dophuong.identity_service.dto.request.*;
import com.dophuong.identity_service.dto.response.AuthenticationResponse;
import com.dophuong.identity_service.dto.response.IntrospectResponse;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.entity.InvalidatedToken;
import com.dophuong.identity_service.entity.Role;
import com.dophuong.identity_service.entity.User;
import com.dophuong.identity_service.entity.UserRole;
import com.dophuong.identity_service.enums.ErrorCode;
import com.dophuong.identity_service.exception.AppException;
import com.dophuong.identity_service.mapper.UserMapper;
import com.dophuong.identity_service.repository.InvalidatedTokenRepository;
import com.dophuong.identity_service.repository.RoleRepository;
import com.dophuong.identity_service.repository.UserRepository;
import com.dophuong.identity_service.repository.UserRoleRepository;
import com.dophuong.identity_service.service.AuthenticationService;
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
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private InvalidatedTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserResponse signup(UserCreateRequest request, String roleName) {
        // Chuẩn hóa phone
        String phone = request.getPhone();
        if (phone != null && phone.trim().isEmpty()) {
            phone = null;
        }

        // Check trùng username, email, phone
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE);
        }

        // Map từ DTO sang entity
        User user = userMapper.toEntity(request);

        // Khởi tạo Set<UserRole> nếu chưa khởi tạo
        if (user.getUserRoles() == null) {
            user.setUserRoles(new HashSet<>());
        }

        // Set các field mặc định
        user.setPhone(phone);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        // Mã hóa password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lấy role từ DB
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Tạo UserRole và add vào user
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        user.getUserRoles().add(userRole);

        // Lưu user 1 lần, cascade sẽ lưu UserRole
        user = userRepository.save(user);

        // Trả về response DTO
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
    public void addGlobalRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        boolean checkRole = user.getUserRoles().stream()
                        .anyMatch(ur -> ur.getRole().getName().equals(roleName));

        if(checkRole) throw new AppException(ErrorCode.ROlE_EXISTED);

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        user.getUserRoles().add(userRole);
        role.getUserRoles().add(userRole);
        userRoleRepository.save(userRole);
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
            throw new AppException(ErrorCode.LOGIN_FAILED);
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
        log.warn("User roles size: {}", user.getUserRoles() == null ? 0 : user.getUserRoles().size());

        if(user.getUserRoles() == null || user.getUserRoles().isEmpty()){
            return "";
        }

        String rs =  user.getUserRoles().stream()
                .map(userRole -> "ROLE_" + userRole.getRole().getName())
                .collect(Collectors.joining(" "));
        log.warn("COn điên: " + rs);

        return rs;
    }
}
