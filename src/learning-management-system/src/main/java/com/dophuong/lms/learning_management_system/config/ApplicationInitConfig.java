package com.dophuong.lms.learning_management_system.config;

import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.repository.UserRepository;
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

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
          if(userRepository.findByUsername("admin").isEmpty()){
              User user = User.builder()
                      .username("admin")
                      .password(passwordEncoder.encode("Admin123@"))
                      .email("admin123@gmail.com")
                      .role(Role.ADMIN)
                      .isActive(true)
                      .build();
              userRepository.save(user);
              log.warn("Admin đã được tạo mặc định và có mật khẩu là: Admin123@");
          }
        };
    }
}
