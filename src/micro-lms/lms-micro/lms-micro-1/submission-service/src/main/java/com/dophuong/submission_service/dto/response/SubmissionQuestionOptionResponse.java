package com.dophuong.submission_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionQuestionOptionResponse {
    private Long submissionOptionId;
    private String optionText;
    private int optionOrder;
    private boolean chosen;
}
