package com.dophuong.identity_service.service.impl;

import com.dophuong.identity_service.dto.request.RoleRequest;
import com.dophuong.identity_service.dto.response.RoleResponse;
import com.dophuong.identity_service.entity.Role;
import com.dophuong.identity_service.enums.ErrorCode;
import com.dophuong.identity_service.exception.AppException;
import com.dophuong.identity_service.mapper.RoleMapper;
import com.dophuong.identity_service.repository.RoleRepository;
import com.dophuong.identity_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public RoleResponse getRole(int id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if(!roleRepository.findByName(request.getName().trim()).isEmpty()) {
            throw new AppException(ErrorCode.ROlE_EXISTED);
        }
        Role role = roleMapper.toEntity(request);
        role.setName(request.getName().toUpperCase());
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponses(roles);
    }

    @Override
    public RoleResponse updateRole(int id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Kiểm tra trùng tên (ignore case)
        if(!roleRepository.findByName(request.getName()).isEmpty())
            throw new AppException(ErrorCode.ROlE_EXISTED);;

        role.setName(request.getName().toUpperCase()); // Chuẩn hóa chữ hoa
        role.setDescription(request.getDescription());

        role = roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    @Override
    public void deleteRole(int id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roleRepository.delete(role);
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toResponse(role);
    }
}
