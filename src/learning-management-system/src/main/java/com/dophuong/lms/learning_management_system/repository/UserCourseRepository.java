package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.entity.UserCourse;

import java.util.List;

public interface UserCourseRepository {
    UserCourse save(UserCourse userCourse);

    UserCourse findById(Long id);

    List<UserCourse> findByCourseId(Long courseId);

    List<UserCourse> findByUserId(Long userId);

    void deleteById(Long id);

    List<UserCourseResponse> findAllByCourseForUser(Long userId);
}
