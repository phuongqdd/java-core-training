package com.dophuong.submission_service.repository;

import com.dophuong.submission_service.dto.request.AnswerRequest;
import com.dophuong.submission_service.entity.SubmissionQuestionOption;

import java.util.List;

public interface SubmissionQuestionOptionRepository {
    Long findCorrectInQuestion(Long submissionQuestionId);
    List<SubmissionQuestionOption> findBySubQuesId(Long submissionQuestionId);

    void updateOptionChosen(AnswerRequest answer);
}
