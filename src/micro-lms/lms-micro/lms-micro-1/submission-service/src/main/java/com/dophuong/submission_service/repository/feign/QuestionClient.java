package com.dophuong.submission_service.repository.feign;

import com.dophuong.submission_service.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "question-service",
        url = "http://localhost:8083",
        configuration = AuthenticationRequestInterceptor.class
)
public interface QuestionClient {
    @GetMapping("/internal/courses/{courseId}/questions/{questionId}/exists")
    ResponseEntity<Boolean> exists(@PathVariable Long courseId, @PathVariable Long questionId);

    @GetMapping("/internal/courses/{courseId}/questions/total")
    ResponseEntity<Integer> getToTal(@PathVariable("courseId") Long courseId);

    @GetMapping("/internal/courses/{courseId}/questions/level/{level}")
    ResponseEntity<List<Long>> getQuestionsByLevel(@PathVariable Long courseId, @PathVariable String level);

    @GetMapping("/internal/courses/{courseId}/questions/{questionId}/level")
    ResponseEntity<String> getLevelByQuestionId(@PathVariable Long courseId, @PathVariable Long questionId);
}

