package com.dophuong.course_service.config;

import com.dophuong.course_service.repository.CourseRepository;
import com.dophuong.course_service.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component("courseSecurity")
public class CourseSecurity {

    @Autowired
    private CourseService courseService;

    private String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /**
     * Kiểm tra user hiện tại có nằm trong khóa học không
     */
    public boolean isUserInCourse(Long courseId) {
        String username = getUserName();
        return courseService.checkUserInCourse(courseId, username);
    }

    /**
     * Kiểm tra user hiện tại có vai trò INSTRUCTOR trong khóa học không
     */
    public boolean hasInstructorInCourse(Long courseId) {
        String username = getUserName();

        if (!courseService.checkUserInCourse(courseId, username)) {
            return false;
        }
        return courseService.checkRoleInCourse(courseId, username);
    }
}
