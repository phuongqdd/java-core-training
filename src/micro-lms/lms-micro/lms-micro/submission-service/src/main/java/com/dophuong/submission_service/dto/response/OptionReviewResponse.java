package com.dophuong.submission_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionReviewResponse {
    private Long submissionOptionId;
    private String optionText;
    private Integer optionOrder;
    private Boolean correct;  // Đáp án đúng
    private Boolean chosen;   // Học viên chọn
}