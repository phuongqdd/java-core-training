package com.dophuong.lms.learning_management_system.dto.request;

import com.dophuong.lms.learning_management_system.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserUpdateProfileRequest {
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
}
