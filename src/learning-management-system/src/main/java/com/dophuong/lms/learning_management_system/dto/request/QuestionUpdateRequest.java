package com.dophuong.lms.learning_management_system.dto.request;

import com.dophuong.lms.learning_management_system.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionUpdateRequest {
    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    @Size(min = 5, message = "Nội dung câu hỏi phải có ít nhất 5 ký tự")
    private String content;

    @NotNull(message = "Mức độ khó (difficulty) là bắt buộc")
    private Difficulty difficulty;

    private String imageUrl;

    private String explanation;

    @NotEmpty(message = "Câu hỏi phải có ít nhất 1 option")
    private List<OptionUpdateRequest> options;
}
