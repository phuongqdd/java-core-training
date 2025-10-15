package com.dophuong.quiz_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuizDetailResponse {
    private Long id;
    private String courseTitle;
    private String title;
    private String description;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private int timeLimit;
    private int attemptsAllowed;
    private int total;
    private float passMark;
    private boolean allowReview;
    private boolean isPublished;
    private List<QuestionResponse> questionResponses;
}
