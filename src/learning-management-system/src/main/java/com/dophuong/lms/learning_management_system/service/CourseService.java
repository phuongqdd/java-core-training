package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.enums.Role;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    CourseCreateResponse createCourse(@Valid CourseCreateRequest request);

    List<UserCourseResponse> getAllCourseById(Long id);

    List<UserInCourseResponse> getAllUsersInCourse(Long courseId);

    void addUserToCourse(Long courseId, Long userId, Role role);

    void updateUserRole(Long courseId, Long userId, Role role);
}
