package com.dophuong.question_service.controller;

import com.dophuong.question_service.dto.request.QuestionRequest;
import com.dophuong.question_service.dto.request.QuestionUpdateRequest;
import com.dophuong.question_service.dto.response.ApiResponse;
import com.dophuong.question_service.dto.response.QuestionOnlyResponse;
import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.enums.Difficulty;
import com.dophuong.question_service.service.QuestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses/{courseId}/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

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
    public ResponseEntity<ApiResponse<Page<QuestionOnlyResponse>>> getAllQuestions(
            @PathVariable Long courseId,
            @PageableDefault(size = 20, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<QuestionOnlyResponse> response = questionService.getAllQuestion(courseId, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<QuestionOnlyResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách câu hỏi thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long courseId,
            @PathVariable Long questionId,
            @RequestBody QuestionUpdateRequest request
    ) {
        QuestionResponse response = questionService.updateQuestion(courseId, questionId, request);


        return ResponseEntity.ok(
                ApiResponse.<QuestionResponse>builder()
                        .status(HttpStatus.OK.value())  // dùng .value() nếu là int
                        .message("Cập nhật câu hỏi thành công!")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long courseId,
            @PathVariable Long questionId
    ) {
        questionService.deleteQuestion(courseId, questionId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())   // status code
                .message("Xóa câu hỏi thành công!")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<Difficulty, Integer>> hihi(
            @PathVariable Long courseId) {
        Map<Difficulty, Integer> rs = questionService.getQuestionsByDifficulty(courseId);
        return ResponseEntity.ok(rs);
    }

}
