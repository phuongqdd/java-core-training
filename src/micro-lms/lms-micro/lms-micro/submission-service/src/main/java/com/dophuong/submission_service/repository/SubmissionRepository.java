package com.dophuong.submission_service.repository;

import com.dophuong.submission_service.dto.response.QuestionResponse;
import com.dophuong.submission_service.dto.response.QuizDetailResponse;
import com.dophuong.submission_service.dto.response.SubmissionResponse;
import com.dophuong.submission_service.entity.Submission;

import java.util.List;

public interface SubmissionRepository {

    boolean exist(Long id);

    SubmissionResponse createSubmission(Long courseId, Long quizId, Long userId);

    SubmissionResponse traKetQua(Long courseId, Long quizId, Long submissionId);

    int countQuizAttempts(Long userId, Long quizId);

    Submission getInProgressSubmission(Long userId, Long quizId);

    void finishSubmission(Long courseId, Long quizId, Long submissionId);

    SubmissionResponse daoDe(Long quizId, Long userId, int atpList, Long courseId, List<QuestionResponse> questionResponseList);

    List<SubmissionResponse> getAllSubmission(List<QuizDetailResponse> quizDetailResponseList);

    int getAttemptNo(Long submissionId);

    void updateGrade(Long submissionId, double score);

    Submission findById(Long submissionId);
}
