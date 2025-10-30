package com.dophuong.quiz_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuizSummaryResponse {
    private Long id;
    private String title;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private int timeLimit;
    private boolean published;
}
