package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private Course course;
    private String content;
    private Difficulty difficulty;
    private String imageUrl;
    private String explanation;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
