package com.dophuong.submission_service.repository;

import com.dophuong.submission_service.dto.response.QuestionResponse;
import com.dophuong.submission_service.entity.SubmissionQuestion;

import java.util.List;

public interface SubmissionQuestionRepository {
    List<SubmissionQuestion> findBySubmissionId(Long submissionId);

    void updateQuestionRs(Long submissionQuestionId, int check);
}
