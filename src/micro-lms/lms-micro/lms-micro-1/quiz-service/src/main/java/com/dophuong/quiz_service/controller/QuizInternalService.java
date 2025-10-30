package com.dophuong.quiz_service.controller;

import com.dophuong.quiz_service.dto.response.QuestionResponse;
import com.dophuong.quiz_service.dto.response.QuizResponse;
import com.dophuong.quiz_service.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/internal/courses/{courseId}/quizzes/{quizId}")
public class QuizInternalService {

    @Autowired
    private QuizService quizService;

    @GetMapping
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable Long quizId){
        return ResponseEntity.ok(quizService.getQuiz(quizId));
    }

    @GetMapping("/exists-quizId")
    public ResponseEntity<Boolean> existsQuizId(@PathVariable Long quizId){
        return ResponseEntity.ok(quizService.exists(quizId));
    }

    @GetMapping("/exists-courseId-quizId")
    public ResponseEntity<Boolean> existsByCourseIdAndQuizId(@PathVariable Long courseId, @PathVariable Long quizId){
        return ResponseEntity.ok(quizService.existsByCourseIdAndQuizId(courseId, quizId));
    }

    @GetMapping("/attempts")
    public ResponseEntity<Integer> getAttempts(@PathVariable Long quizId){
        return ResponseEntity.ok(quizService.getAttempts(quizId));
    }

    @GetMapping("/question-detail")
    public ResponseEntity<List<QuestionResponse>> getAllQuestionDetailByQuizId(@PathVariable Long courseId, @PathVariable Long quizId){
        return ResponseEntity.ok(quizService.getAllQuestionDetailByQuizId(courseId, quizId));
    }

}
