package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findByUserId(Long userId);

    List<UserCourse> findByCourseId(Long courseId);

    Optional<UserCourse> findByUserIdAndCourseId(Long userId, Long courseId);
    Optional<UserCourse> findByCourseIdAndUserId(Long courseId, Long userId);
}
