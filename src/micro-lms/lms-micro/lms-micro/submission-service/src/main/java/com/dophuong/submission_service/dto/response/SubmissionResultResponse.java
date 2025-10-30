package com.dophuong.submission_service.dto.response;

import com.dophuong.submission_service.entity.SubmissionQuestion;
import com.dophuong.submission_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResultResponse {
    private Long submissionId;
    private int attemptNo;
    private Double score;
}
