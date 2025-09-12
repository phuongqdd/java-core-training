package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT uc.course FROM UserCourse uc WHERE uc.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") Long userId);
}
