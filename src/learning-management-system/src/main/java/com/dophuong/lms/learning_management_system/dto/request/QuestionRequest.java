package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {
    @NotBlank(message = "Nội dung câu hỏi là bắt buộc")
    private String content;

    @NotNull(message = "Độ khó là bắt buộc")
    private String difficulty; // Enum dạng String

    private String imageUrl;

    private String explanation;

    @NotNull(message = "Options không được để rỗng")
    private List<OptionRequest> options;
}
