package com.dophuong.submission_service.config;

import com.dophuong.submission_service.repository.feign.CourseClient;
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

        if (!Boolean.TRUE.equals(courseClient.exists(courseId, username).getBody())) {
            return false;
        }

        return Boolean.TRUE.equals(courseClient.hasRole(courseId, username).getBody());
    }

    public boolean isUserInCourse(Long courseId) {
        String username = getUserName();
        return Boolean.TRUE.equals(courseClient.exists(courseId, username).getBody());
    }
}
