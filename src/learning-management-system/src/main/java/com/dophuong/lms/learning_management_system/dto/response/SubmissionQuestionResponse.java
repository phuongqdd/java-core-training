package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubmissionQuestionResponse {
    private Long submissionQuestionId;
    private Long questionId;
    private String questionText;
    private int questionOrder;
    private List<SubmissionQuestionOptionResponse> options;
}
