package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String fullName;
    private String username;
    private String avatarUrl;
    private Role role;
    private Boolean isOwner;
}
