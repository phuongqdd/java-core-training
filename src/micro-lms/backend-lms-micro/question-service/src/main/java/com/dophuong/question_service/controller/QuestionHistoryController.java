package com.dophuong.question_service.controller;

import com.dophuong.question_service.dto.response.ApiResponse;
import com.dophuong.question_service.dto.response.QuestionHistoryResponse;
import com.dophuong.question_service.service.QuestionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/course/{courseId}/questions/{questionId}/history")
@RequiredArgsConstructor
public class QuestionHistoryController {

    private final QuestionHistoryService historyService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<List<QuestionHistoryResponse>>> getQuestionHistory(
            @PathVariable Long courseId,
            @PathVariable Long questionId
    ) {
        List<QuestionHistoryResponse> history = historyService.getHistoryByQuestionId(questionId, courseId);
        return ResponseEntity.ok(
                ApiResponse.<List<QuestionHistoryResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy lịch sử thay đổi thành công")
                        .data(history)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
