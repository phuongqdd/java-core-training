package com.dophuong.lms.learning_management_system.repository;


import com.dophuong.lms.learning_management_system.dto.response.SubmissionResponse;

public interface SubmissionRepository {

    SubmissionResponse createSubmission(Long quizId, Long userId);

    int countQuizAttempts(Long userId, Long quizId);
}
