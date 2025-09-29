package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.AddUserToCourseRequest;
import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.AddUserToCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.enums.Role;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {

    Course getCourse(Long id);

    boolean existsById(Long id);

    CourseCreateResponse createCourse(@Valid CourseCreateRequest request, String name);

    List<UserCourseResponse> getAllCourseById(Long userId);

    List<UserInCourseResponse> getAllUsersInCourse(Long courseId);

    void updateUserRole(Long courseId, Long userId, Role role);

    AddUserToCourseResponse addUserToCourse(Long courseId, AddUserToCourseRequest request);
}
