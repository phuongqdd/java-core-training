package com.dophuong.submission_service.service;

import com.dophuong.submission_service.dto.request.SubmissionRequest;
import com.dophuong.submission_service.dto.response.OptionReviewResponse;
import com.dophuong.submission_service.dto.response.SubmissionResponse;
import com.dophuong.submission_service.dto.response.SubmissionResultResponse;
import com.dophuong.submission_service.dto.response.SubmissionReviewResponse;

import java.util.List;

public interface SubmissionService {
    SubmissionResponse startSubmission(Long courseId, Long quizId);

    SubmissionResultResponse gradeSubmission(Long quizId, Long submissionId, SubmissionRequest submissionRequest);

    SubmissionReviewResponse reviewSubmission(Long submissionId);
}
