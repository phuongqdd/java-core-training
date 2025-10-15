package com.dophuong.identity_service.dto.request;

import com.dophuong.identity_service.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private Role role;
}
