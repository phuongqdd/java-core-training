package com.dophuong.course_service.mapper;

import com.dophuong.course_service.dto.request.CourseCreateRequest;
import com.dophuong.course_service.dto.response.CourseResponse;
import com.dophuong.course_service.entity.Course;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toEntity(CourseCreateRequest request);

    CourseResponse toResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> course);
}
