package com.dophuong.submission_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionQuestionResponse {
    private Long submissionQuestionId;
    private String questionText;
    private int questionOrder;
    private List<SubmissionQuestionOptionResponse> options;
}
