package com.dophuong.course_service.controller;

import com.dophuong.course_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/courses")
public class CourseInternalController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/get-course-id")
    public ResponseEntity<List<Long>> getCourseId(){
        return ResponseEntity.ok(courseService.getAllCourseIds());
    }

    @GetMapping("/{courseId}/members/{username}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long courseId, @PathVariable String username){
        boolean rs = courseService.checkUserInCourse(courseId, username);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/{courseId}/members/{username}/has-role")
    public ResponseEntity<Boolean> hasRole(@PathVariable Long courseId, @PathVariable String username){
        boolean rs = courseService.checkRoleInCourse(courseId, username);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/{courseId}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long courseId){
        boolean ex = courseService.existsById(courseId);
        return ResponseEntity.ok(ex);
    }
}
