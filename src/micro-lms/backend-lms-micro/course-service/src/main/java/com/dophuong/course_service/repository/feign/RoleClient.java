package com.dophuong.course_service.repository.feign;

import com.dophuong.course_service.config.AuthenticationRequestInterceptor;
import com.dophuong.course_service.dto.response.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service",
        contextId = "roleClient",
        url = "http://localhost:8080/internal/role",
        configuration = AuthenticationRequestInterceptor.class)
public interface RoleClient {
    @GetMapping("/{roleId}")
    ResponseEntity<RoleResponse> getRoleById(@PathVariable int roleId);

    @GetMapping("/by-name/{name}")
    ResponseEntity<RoleResponse> getByRoleName(@PathVariable String name);
}
