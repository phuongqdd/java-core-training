package com.dophuong.lms.learning_management_system.config;

import com.dophuong.lms.learning_management_system.repository.CourseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("courseSecurity")
public class CourseSecurity {
    @Autowired
    private CourseJdbcRepository courseJdbcRepository;

    public boolean hasInstructorOrAdmin(Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Check user có role ADMIN toàn hệ thống
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        // Check user có role INSTRUCTOR trong khóa học
        return courseJdbcRepository.checkRoleInCourse(courseId, username);
    }
}
