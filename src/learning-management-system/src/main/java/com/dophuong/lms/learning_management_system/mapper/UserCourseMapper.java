package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserCourseMapper {
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "name")
    @Mapping(source = "course.description", target = "description")
    @Mapping(source = "course.thumbnailUrl", target = "thumbnailUrl")
    @Mapping(source = "course.capacity", target = "capacity")
    @Mapping(source = "course.isPublished", target = "isPublished")
    @Mapping(source = "course.createdAt", target = "createdAt")

    @Mapping(source = "enrolledAt", target = "enrolledAt")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "isOwner", target = "isOwner")
    UserCourseResponse toResponse(UserCourse userCourse);
}
