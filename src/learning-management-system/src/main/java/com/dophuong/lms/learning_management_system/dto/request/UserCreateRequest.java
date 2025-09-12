package com.dophuong.lms.learning_management_system.dto.request;

import com.dophuong.lms.learning_management_system.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank(message = "Username là bắt buộc")
    private String username;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Password là bắt buộc")
    private String password;

    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
}
