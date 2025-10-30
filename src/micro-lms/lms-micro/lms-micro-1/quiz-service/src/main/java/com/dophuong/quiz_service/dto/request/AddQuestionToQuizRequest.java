package com.dophuong.quiz_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddQuestionToQuizRequest {
    @NotNull(message = "QuestionId là trường bắt buộc")
    private Long questionId;
}
