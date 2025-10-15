package com.dophuong.submission_service.controller;

import com.dophuong.submission_service.dto.response.ApiResponse;
import com.dophuong.submission_service.dto.response.SubmissionResponse;
import com.dophuong.submission_service.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
}
