package com.dophuong.identity_service.mapper;

import com.dophuong.identity_service.dto.request.UserCreateRequest;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.entity.User;
import com.dophuong.identity_service.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequest request);
    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);

    default Set<String> mapRoles(User user) {
        if (user.getUserRoles() == null) return Set.of();
        return user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());
    }
}
