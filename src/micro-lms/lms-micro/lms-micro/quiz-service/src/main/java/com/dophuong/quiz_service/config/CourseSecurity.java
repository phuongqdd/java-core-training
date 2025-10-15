package com.dophuong.quiz_service.config;

import com.dophuong.quiz_service.repository.feign.CourseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component("courseSecurity")
public class CourseSecurity {
    @Autowired
    private CourseClient courseClient;

    private String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


    public boolean hasInstructorInCourse(Long courseId) {
        String username = getUserName();
        // Check user có role INSTRUCTOR trong khóa học
        log.warn("Role: {}", username);
        log.warn("COurseId: {}", courseId);
        return Boolean.TRUE.equals(courseClient.exists(courseId, username).getBody());
    }

    // Check user có trong khóa học hay không
    public boolean isUserInCourse(Long courseId) {
        String username = getUserName();
        log.warn("COurseId: {}", courseId);
        return Boolean.TRUE.equals(courseClient.hasRole(courseId, username).getBody());
    }
}
