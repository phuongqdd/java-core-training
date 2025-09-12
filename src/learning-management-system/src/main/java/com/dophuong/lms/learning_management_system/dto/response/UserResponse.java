package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.enums.Gender;
import com.dophuong.lms.learning_management_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private Boolean isActive;
    private Role role;
    private Gender gender;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
