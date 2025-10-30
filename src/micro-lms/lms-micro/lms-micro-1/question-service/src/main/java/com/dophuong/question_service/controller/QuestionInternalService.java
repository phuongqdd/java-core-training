package com.dophuong.question_service.controller;

import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.enums.Difficulty;
import com.dophuong.question_service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/courses/{courseId}/questions")
@RequiredArgsConstructor
public class QuestionInternalService {

    private final QuestionService questionService;

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestion(
            @PathVariable Long questionId
    ) {
        QuestionResponse response = questionService.getQuestion(questionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getToTal(@PathVariable Long courseId){
        return ResponseEntity.ok(questionService.getTotalQuestions(courseId));
    }

    @GetMapping("/get-diff")
    public ResponseEntity<Map<Difficulty, Integer>> getDiff(
            @PathVariable Long courseId) {
        Map<Difficulty, Integer> rs = questionService.getQuestionsByDifficulty(courseId);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("{questionId}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long questionId){
        return ResponseEntity.ok(questionService.existsQuestion(questionId));
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<Long>> getQuestionsByLevel(@PathVariable Long courseId, @PathVariable String level){
        return ResponseEntity.ok(questionService.getQuestionsByLevel(courseId, level));
    }

    @GetMapping("{questionId}/level")
    public ResponseEntity<String> getLevelByQuestionId(@PathVariable Long questionId){
        return ResponseEntity.ok(questionService.getLevelByQuestionId(questionId));
    }
}
