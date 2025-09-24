package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionResponse;

public interface SubmissionService {
    SubmissionResponse startSubmission(Long courseId, Long quizId);
}
