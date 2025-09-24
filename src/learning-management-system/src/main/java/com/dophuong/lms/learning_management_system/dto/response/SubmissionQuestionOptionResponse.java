package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmissionQuestionOptionResponse {
    private Long submissionOptionId;
    private Long optionId;
    private String optionText;
    private int optionOrder;
    private boolean chosen;
}
