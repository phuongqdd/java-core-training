package com.dophuong.question_service.dto.response;

import com.dophuong.question_service.enums.Difficulty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private String content;
    private Difficulty difficulty;
    private String imageUrl;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OptionResponse> options;
}
