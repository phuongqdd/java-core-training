package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
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

    default String getOwnerSummary(Course course) {
        if (course == null || course.getUserCourses() == null || course.getUserCourses().isEmpty()) {
            return null;
        }

        UserCourse ownerUc = course.getUserCourses().get(0); // chỉ lấy người đầu tiên
        if (ownerUc == null || ownerUc.getUser() == null) return null;

        return ownerUc.getUser().getUsername();
    }


    CourseResponse toCuCourseResponse(Course course);
    List<CourseResponse> toResponseList(List<Course> course);
}
