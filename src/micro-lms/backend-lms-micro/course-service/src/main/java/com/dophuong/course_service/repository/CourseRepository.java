package com.dophuong.course_service.repository;


import com.dophuong.course_service.entity.Course;
import com.dophuong.course_service.entity.UserCourse;
import com.dophuong.course_service.enums.Role;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findById(Long id);

    List<Course> findAll();

    void deleteById(Long id);

    void addUserToCourse(Long courseId, Long userId, String role);

    UserCourse getUserCourse(Long courseId, Long userId);

    boolean checkRoleInCourse(Long courseId, Long userId);

    boolean checkUserInCourse(Long courseId, Long userId);

    boolean existsById(Long id);

    void updateUserRole(Long courseId, Long userId, String role);
}
