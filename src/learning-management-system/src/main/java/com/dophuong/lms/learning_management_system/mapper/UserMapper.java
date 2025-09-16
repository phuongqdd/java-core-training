package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.RoleResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.Role;
import com.dophuong.lms.learning_management_system.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);

    RoleResponse toResponse(Role role);

    @AfterMapping
    default void mapRoles(User user, @MappingTarget UserResponse response){
        if (user.getUserRoles() != null) {
            Set<RoleResponse> roles = user.getUserRoles().stream()
                    .map(userRole -> toResponse(userRole.getRole()))
                    .collect(Collectors.toSet());
            response.setRoles(roles);
        }
    }
}
