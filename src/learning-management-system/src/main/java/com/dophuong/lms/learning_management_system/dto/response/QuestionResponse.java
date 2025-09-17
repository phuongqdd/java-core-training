package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private String content;
    private String difficulty;
    private String imageUrl;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OptionResponse> options;
}
