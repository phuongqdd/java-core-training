package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizResponse;
import com.dophuong.lms.learning_management_system.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/courses/{courseId}/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @Valid @RequestBody QuizCreateRequest request
            ){
        QuizResponse quizResponse = quizService.createQuiz(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(ApiResponse.<QuizResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo mới bài kiểm tra thành công")
                        .data(quizResponse)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/{quizId}/details")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuizDetailResponse>> getQuizDetail(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId
    ){
        QuizDetailResponse quizDetailResponse = quizService.getQuizDetail(courseId, quizId);
        return ResponseEntity.ok(ApiResponse.<QuizDetailResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy bài kiểm tra thành công")
                        .data(quizDetailResponse)
                        .timestamp(LocalDateTime.now())
                .build());
    }

}
