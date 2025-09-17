package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.RoleRequest;
import com.dophuong.lms.learning_management_system.dto.response.RoleResponse;
import com.dophuong.lms.learning_management_system.entity.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);
    RoleResponse toResponse(Role role);
    Set<RoleResponse> toResponseSet(Set<Role> roles);
    List<RoleResponse> toResponses(List<Role> role);
}
