package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.RoleRequest;
import com.dophuong.lms.learning_management_system.dto.response.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService {
    RoleResponse getRole(int id);

    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRole(int id, @Valid RoleRequest request);

    void deleteRole(int id);
}
