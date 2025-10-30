package com.dophuong.identity_service.mapper;

import com.dophuong.identity_service.dto.request.RoleRequest;
import com.dophuong.identity_service.dto.response.RoleResponse;
import com.dophuong.identity_service.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);
    RoleResponse toResponse(Role role);
    Set<RoleResponse> toResponseSet(Set<Role> roles);
    List<RoleResponse> toResponses(List<Role> role);
}
