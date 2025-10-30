package com.dophuong.question_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionEvent {
    private Long courseId;
    private Long questionId;             // ID của câu hỏi
    private String difficulty;   // "easy", "medium", "hard"
    private String action;       // "ADD", "UPDATE", "DELETE"
    private String oldDifficulty;
}
