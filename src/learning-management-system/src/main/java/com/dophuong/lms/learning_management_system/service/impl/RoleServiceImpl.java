package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.RoleRequest;
import com.dophuong.lms.learning_management_system.dto.response.RoleResponse;
import com.dophuong.lms.learning_management_system.entity.Role;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.RoleMapper;
import com.dophuong.lms.learning_management_system.repository.RoleRepository;
import com.dophuong.lms.learning_management_system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
