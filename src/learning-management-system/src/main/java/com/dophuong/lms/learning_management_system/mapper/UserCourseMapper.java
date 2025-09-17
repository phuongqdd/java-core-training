package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.AddUserToCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    UserCourseMapper INSTANCE = Mappers.getMapper(UserCourseMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "role.name", target = "role")
    AddUserToCourseResponse toResponse(UserCourse userCourse);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "role.name", target = "role")
    UserInCourseResponse toResponse1(UserCourse userCourse);
}
