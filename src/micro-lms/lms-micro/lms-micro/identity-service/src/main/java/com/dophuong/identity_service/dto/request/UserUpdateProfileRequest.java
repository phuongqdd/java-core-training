package com.dophuong.identity_service.dto.request;

import com.dophuong.identity_service.enums.Gender;
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
