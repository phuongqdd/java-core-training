package com.dophuong.submission_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubmissionReviewResponse {
    private Long submissionId;
    private Long quizId;
    private Double score;
    private Integer attemptNo;
    private List<QuestionReviewResponse> questions;
}