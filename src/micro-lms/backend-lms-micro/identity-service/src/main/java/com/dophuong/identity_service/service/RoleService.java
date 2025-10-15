package com.dophuong.identity_service.service;

import com.dophuong.identity_service.dto.request.RoleRequest;
import com.dophuong.identity_service.dto.response.RoleResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface RoleService {
    RoleResponse getRole(int id);

    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRole(int id, @Valid RoleRequest request);

    void deleteRole(int id);

    RoleResponse getRoleByName(String name);
}
