package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.UserCourse;

import java.util.List;
import java.util.Optional;

public interface CourseJdbcRepository {

    Course save(Course course);

    Optional<Course> findById(Long id);

    List<Course> findAll();

    void deleteById(Long id);

    public int addUserToCourse(Long courseId, Long userId, String role);

    public UserCourse getUserCourse(Long courseId, Long userId);

    public boolean checkRoleInCourse(Long courseId, String name);

    boolean checkUserInCourse(Long courseId, String username);
}
