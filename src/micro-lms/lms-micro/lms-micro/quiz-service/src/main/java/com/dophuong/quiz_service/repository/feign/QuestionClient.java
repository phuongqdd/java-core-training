package com.dophuong.quiz_service.repository.feign;

import com.dophuong.quiz_service.config.AuthenticationRequestInterceptor;
import com.dophuong.quiz_service.dto.response.QuestionResponse;
import com.dophuong.quiz_service.enums.Difficulty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/internal/courses/{courseId}/questions/{questionId}")
    ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long courseId, @PathVariable Long questionId);

    @GetMapping("/internal/courses/{courseId}/questions/{questionId}/exists")
    ResponseEntity<Boolean> exists(@PathVariable Long courseId, @PathVariable Long questionId);

    @GetMapping("/internal/courses/{courseId}/questions/total")
    ResponseEntity<Integer> getToTal(@PathVariable("courseId") Long courseId);

    @GetMapping("/internal/courses/{courseId}/questions/get-diff")
    ResponseEntity<Map<Difficulty, Integer>> getDiff(@PathVariable("courseId") Long courseId);

    @GetMapping("/internal/courses/{courseId}/questions/level/{level}")
    ResponseEntity<List<Long>> getQuestionsByLevel(@PathVariable Long courseId, @PathVariable String level);

    @GetMapping("/internal/courses/{courseId}/questions/{questionId}/level")
    ResponseEntity<String> getLevelByQuestionId(@PathVariable Long courseId, @PathVariable Long questionId);
}

