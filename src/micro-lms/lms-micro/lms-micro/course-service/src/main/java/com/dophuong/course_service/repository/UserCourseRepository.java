package com.dophuong.course_service.repository;

import com.dophuong.course_service.dto.response.UserCourseResponse;
import com.dophuong.course_service.entity.UserCourse;

import java.util.List;

public interface UserCourseRepository {
    UserCourse save(UserCourse userCourse);

    UserCourse findById(Long id);

    List<UserCourse> findByCourseId(Long courseId);

    List<UserCourse> findByUserId(Long userId);

    void deleteById(Long id);

    List<UserCourseResponse> findAllByCourseForUser(Long userId);
}
