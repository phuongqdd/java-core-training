package com.dophuong.question_service.repository.feign;

import com.dophuong.question_service.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service",
        url = "http://localhost:8081/internal/courses",
    configuration = AuthenticationRequestInterceptor.class)
public interface CourseClient {
    @GetMapping("/get-course-id")
    ResponseEntity<List<Long>> getCourseId();

    @GetMapping("/{courseId}/members/{username}/exists")
    ResponseEntity<Boolean> exists(@PathVariable Long courseId, @PathVariable String username);

    @GetMapping("/{courseId}/members/{username}/has-role")
    ResponseEntity<Boolean> hasRole(@PathVariable Long courseId, @PathVariable String username);

    @GetMapping("/{courseId}/exists")
    ResponseEntity<Boolean> exists(@PathVariable Long courseId);
}
