package com.dophuong.submission_service.dto.response;

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
