package com.dophuong.lms.learning_management_system.dto.request;

import com.dophuong.lms.learning_management_system.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private Role role;
}
