package com.dophuong.submission_service.dto.response;

import com.dophuong.submission_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionResponse {
    private Long submissionId;
    private Long quizId;
    private String quizTitle;
    private int attemptNo;
    private Status status;
    private int timeLimit;
    private Long userId;
    private LocalDateTime startedAt;
    private List<SubmissionQuestionResponse> questions;
}
