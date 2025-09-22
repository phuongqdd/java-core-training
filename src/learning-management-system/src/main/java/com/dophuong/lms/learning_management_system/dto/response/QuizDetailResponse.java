package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizDetailResponse {
    private QuizResponse quizResponse;
    private List<QuestionResponse> questionResponses;
}
