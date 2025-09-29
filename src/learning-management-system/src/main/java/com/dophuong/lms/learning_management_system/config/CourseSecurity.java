package com.dophuong.lms.learning_management_system.config;

import com.dophuong.lms.learning_management_system.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("courseSecurity")
public class CourseSecurity {
    @Autowired
    private CourseRepository courseRepository;

    private String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public boolean hasInstructorInCourse(Long courseId) {
        String username = getUserName();
        // Check user có role INSTRUCTOR trong khóa học
        return courseRepository.checkRoleInCourse(courseId, username);
    }

    // Check user có trong khóa học hay không
    public boolean isUserInCourse(Long courseId) {
        String username = getUserName();
        return courseRepository.checkUserInCourse(courseId, username);
    }
}
