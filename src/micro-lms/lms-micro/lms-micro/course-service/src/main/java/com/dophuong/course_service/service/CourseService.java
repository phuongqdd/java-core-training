package com.dophuong.course_service.service;

import com.dophuong.course_service.dto.request.AddUserToCourseRequest;
import com.dophuong.course_service.dto.request.CourseCreateRequest;
import com.dophuong.course_service.dto.response.AddUserToCourseResponse;
import com.dophuong.course_service.dto.response.CourseResponse;
import com.dophuong.course_service.dto.response.UserCourseResponse;
import com.dophuong.course_service.dto.response.UserInCourseResponse;
import com.dophuong.course_service.entity.Course;
import com.dophuong.course_service.enums.Role;

import java.util.List;

public interface CourseService {

    Course getCourse(Long id);

    boolean existsById(Long id);

    CourseResponse createCourse(CourseCreateRequest request, String username);

    List<UserCourseResponse> getAllCourse();

    List<UserInCourseResponse> getAllUsersInCourse(Long courseId);

    void updateUserRole(Long courseId, Long userId, String role);

    void addUserToCourse(Long courseId, AddUserToCourseRequest request);

    boolean checkRoleInCourse(Long courseId, String username);

    boolean checkUserInCourse(Long courseId, String username);

    List<Long> getAllCourseIds();
}
