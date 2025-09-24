package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.entity.SubmissionQuestion;
import com.dophuong.lms.learning_management_system.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SubmissionResponse {
    private Long submissionId;
    private Long quizId;
    private String quizTitle;
    private int attemptNo;
    private Status status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private int duration;
    private List<SubmissionQuestion> questions;
}
