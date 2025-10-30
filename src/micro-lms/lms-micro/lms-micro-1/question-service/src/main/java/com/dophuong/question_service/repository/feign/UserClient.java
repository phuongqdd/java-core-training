package com.dophuong.question_service.repository.feign;

import com.dophuong.question_service.config.AuthenticationRequestInterceptor;
import com.dophuong.question_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service",
        contextId = "userClient",
        url = "http://localhost:8080/internal/users",
        configuration = AuthenticationRequestInterceptor.class)
public interface UserClient {
    @GetMapping("/by-username/{username}")
    ResponseEntity<UserResponse> getByUsername(@PathVariable String username);
}
