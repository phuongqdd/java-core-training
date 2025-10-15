package com.dophuong.question_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionHistoryResponse {
    private Long id;
    private String actionType;
    private Long questionId;
    private String title;
    private Long userId;
    private String username;
    private LocalDateTime time;
}
