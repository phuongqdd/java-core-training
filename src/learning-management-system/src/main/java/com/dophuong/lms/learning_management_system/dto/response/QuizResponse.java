package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuizResponse {
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
}
