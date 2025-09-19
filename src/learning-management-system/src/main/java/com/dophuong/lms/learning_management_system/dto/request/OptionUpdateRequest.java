package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionUpdateRequest {
    private Long id;

    @NotBlank(message = "Nội dung đáp án không được để trống")
    private String content;

    @NotNull(message = "Phải xác định đáp án đúng hay sai")
    private Boolean correct;
}
