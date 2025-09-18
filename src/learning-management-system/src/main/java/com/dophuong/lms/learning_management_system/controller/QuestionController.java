package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.mapper.OptionMapper;
import com.dophuong.lms.learning_management_system.mapper.QuestionMapper;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private OptionMapper optionMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
            @PathVariable Long courseId,
            @Valid @RequestBody QuestionRequest request
    ) {
        QuestionResponse response = questionService.createQuestion(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<QuestionResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Thêm mới câu hỏi thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestion(
            @PathVariable Long courseId,
            @PathVariable Long questionId
    ) {
        QuestionResponse response = questionService.getQuestion(courseId, questionId);
        return ResponseEntity.ok(
                ApiResponse.<QuestionResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy câu hỏi thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getAllQuestions(
            @PathVariable Long courseId
    ) {
        List<QuestionResponse> response = questionService.getAllQuestion(courseId);
        return ResponseEntity.ok(
                ApiResponse.<List<QuestionResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách câu hỏi thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/test")
    public ResponseEntity<String> hihi(
            @Valid @RequestBody Question request
    ) {
        QuestionResponse response = questionMapper.toResponse(request);
        return ResponseEntity.ok(response.getUpdatedAt() + " ");
    }


}
