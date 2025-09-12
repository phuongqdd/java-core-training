package com.dophuong.lms.learning_management_system.config;

import com.dophuong.lms.learning_management_system.dto.request.IntrospectRequest;
import com.dophuong.lms.learning_management_system.dto.response.IntrospectResponse;
import com.dophuong.lms.learning_management_system.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    // NimbusJwtDecoder dùng để decode token JWT thành đối tượng Jwt
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        // Kiểm tra token hợp lệ
        IntrospectResponse response = authenticationService.introspectResponse(
                IntrospectRequest.builder()
                        .token(token)
                        .build()
        );

        if (!response.isValid()) {
            // Token không hợp lệ → ném exception
            throw new JwtException("Token không hợp lệ hoặc đã bị thu hồi");
        }

        // Khởi tạo NimbusJwtDecoder nếu chưa có
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        // Decode token thành Jwt object
        return nimbusJwtDecoder.decode(token);
    }
}
