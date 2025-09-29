package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.entity.SubmissionQuestion;
import com.dophuong.lms.learning_management_system.enums.Status;
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
    private List<SubmissionQuestionResponse> questions;
}
