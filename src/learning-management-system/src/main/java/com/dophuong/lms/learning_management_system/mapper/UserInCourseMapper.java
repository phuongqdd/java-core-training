package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserInCourseMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "username")
    UserInCourseResponse toResponse(UserCourse userCourse);
}