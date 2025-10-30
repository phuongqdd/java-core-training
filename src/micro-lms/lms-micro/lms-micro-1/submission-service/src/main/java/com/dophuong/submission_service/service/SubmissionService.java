package com.dophuong.submission_service.service;

import com.dophuong.submission_service.dto.response.SubmissionResponse;

public interface SubmissionService {
    SubmissionResponse startSubmission(Long courseId, Long quizId);
}
