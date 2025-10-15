package com.dophuong.identity_service.config;

import com.dophuong.identity_service.dto.request.UserCreateRequest;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.entity.Role;
import com.dophuong.identity_service.repository.RoleRepository;
import com.dophuong.identity_service.repository.UserRepository;
import com.dophuong.identity_service.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository){
        return args -> {
          if(userRepository.findByUsername("admin").isEmpty()){

              Role adminRole = roleRepository.findByName("ADMIN")
                      .orElseGet(() ->  roleRepository.save(Role.builder()
                                      .name("ADMIN")
                                      .description("Quản trị hệ thống")
                              .build()));

              UserCreateRequest request = UserCreateRequest.builder()
                      .username("admin")
                      .password("Admin123@")
                      .email("admin123@gmail.com")
                      .build();
              UserResponse response = authenticationService.signup(request, "ADMIN");
              log.warn("Admin đã được tạo mặc định có username là admin và mật khẩu là: Admin123@");
          }
        };
    }
}
