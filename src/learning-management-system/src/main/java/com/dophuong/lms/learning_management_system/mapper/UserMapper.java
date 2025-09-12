package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
}
