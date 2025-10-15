package com.dophuong.course_service.config;

import com.dophuong.course_service.repository.CourseRepository;
import com.dophuong.course_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("courseSecurity")
public class CourseSecurity {

    @Autowired
    private CourseService courseService;

    private String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public boolean hasInstructorInCourse(Long courseId) {
        String username = getUserName();
        // Check user có role INSTRUCTOR trong khóa học
        return courseService.checkRoleInCourse(courseId, username);
    }

    // Check user có trong khóa học hay không
    public boolean isUserInCourse(Long courseId) {
        String username = getUserName();
        return courseService.checkUserInCourse(courseId, username);
    }
}
