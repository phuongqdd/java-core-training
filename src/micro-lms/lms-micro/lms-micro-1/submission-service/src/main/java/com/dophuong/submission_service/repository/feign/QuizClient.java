package com.dophuong.submission_service.repository.feign;

import com.dophuong.submission_service.config.AuthenticationRequestInterceptor;
import com.dophuong.submission_service.dto.response.QuestionResponse;
import com.dophuong.submission_service.dto.response.QuizResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "quiz-service",
        url = "http://localhost:8082",
        configuration = AuthenticationRequestInterceptor.class
)
public interface QuizClient {

    @GetMapping("/internal/courses/{courseId}/quizzes/{quizId}/exists-quizId")
    ResponseEntity<Boolean> existsQuizId(@PathVariable Long courseId, @PathVariable Long quizId);

    @GetMapping("/internal/courses/{courseId}/quizzes/{quizId}/exists-courseId-quizId")
    ResponseEntity<Boolean> existsByCourseIdAndQuizId(@PathVariable Long courseId, @PathVariable Long quizId);

    @GetMapping("/internal/courses/{courseId}/quizzes/{quizId}/attempts")
    ResponseEntity<Integer> getAttempts(@PathVariable Long courseId, @PathVariable Long quizId);

    @GetMapping("/internal/courses/{courseId}/quizzes/{quizId}")
    ResponseEntity<QuizResponse> getQuiz(@PathVariable Long courseId, @PathVariable Long quizId);

    @GetMapping("/internal/courses/{courseId}/quizzes/{quizId}/question-detail")
    ResponseEntity<List<QuestionResponse>> getAllQuestionDetailByQuizId(@PathVariable Long courseId, @PathVariable Long quizId);
}
