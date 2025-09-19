package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.enums.Difficulty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionOnlyResponse {
    private Long id;
    private String content;
    private Difficulty difficulty;
    private String imageUrl;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
