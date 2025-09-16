package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserSummary;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "isPublished", source = "isPublished")
    Course toEntity(CourseCreateRequest request);

    @Mapping(target = "owner", expression = "java(getOwnerSummary(course))")
    CourseCreateResponse toResponse(Course course);

    default UserSummary getOwnerSummary(Course course){
        if(course.getUserCourses() == null) return null;
        return course.getUserCourses().stream()
                .filter(uc -> uc.getIsOwner() != null && uc.getIsOwner())
                .findFirst()
                .map(uc -> {
                    User u = uc.getUser();
                    UserSummary summary = new UserSummary();
                    summary.setId(u.getId());
                    summary.setUsername(u.getUsername());
//                    summary.setRole(u.getRole());
                    summary.setAvatarUrl(u.getAvatarUrl());
                    summary.setFullName(u.getFullName());
                    summary.setIsOwner(true);
                    return summary;
                })
                .orElse(null);
    }

    CourseResponse toCuCourseResponse(Course course);
    List<CourseResponse> toResponseList(List<Course> course);
}
