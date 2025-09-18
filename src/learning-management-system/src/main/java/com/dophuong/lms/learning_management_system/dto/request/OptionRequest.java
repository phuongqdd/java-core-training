package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionRequest {
    @NotBlank(message = "Nội dung đáp án là bắt buộc")
    private String content;
    private boolean correct;
}
