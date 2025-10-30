package com.dophuong.submission_service.controller;

import com.dophuong.submission_service.dto.request.SubmissionRequest;
import com.dophuong.submission_service.dto.response.*;
import com.dophuong.submission_service.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/quizzes/{quizId}/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isUserInCourse(#courseId)")
    public ResponseEntity<ApiResponse<SubmissionResponse>> startSubmission(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId){
        SubmissionResponse submissionResponse = submissionService.startSubmission(courseId, quizId);

        ApiResponse<SubmissionResponse> response = ApiResponse.<SubmissionResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Bắt đầu làm bài kiểm tra thành công")
                .data(submissionResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @PostMapping("/{submissionId}/submit")
    public ResponseEntity<ApiResponse<SubmissionResultResponse>> submitQuiz(
            @PathVariable Long quizId,
            @PathVariable Long submissionId,
            @RequestBody SubmissionRequest submissionRequest) {

        SubmissionResultResponse result = submissionService.gradeSubmission(quizId, submissionId, submissionRequest);
        return ResponseEntity.ok(ApiResponse.<SubmissionResultResponse>builder()
                        .data(result)
                        .status(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/{submissionId}/review")
    public ResponseEntity<SubmissionReviewResponse> reviewSubmission(
            @PathVariable Long submissionId
    ) {
        SubmissionReviewResponse response = submissionService.reviewSubmission(submissionId);
        return ResponseEntity.ok(response);
    }

}
