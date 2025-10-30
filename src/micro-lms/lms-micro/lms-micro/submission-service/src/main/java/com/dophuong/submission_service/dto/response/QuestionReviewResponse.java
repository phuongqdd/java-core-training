package com.dophuong.submission_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionReviewResponse {
    private Long submissionQuestionId;
    private String questionText;
    private boolean correct;
    private List<OptionReviewResponse> options;
}
