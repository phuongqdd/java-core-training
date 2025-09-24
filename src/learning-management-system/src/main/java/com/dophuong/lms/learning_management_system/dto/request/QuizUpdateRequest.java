package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuizUpdateRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @NotNull(message = "Thời gian mở không được null")
    private LocalDateTime openTime;

    @NotNull(message = "Thời gian đóng không được null")
    private LocalDateTime closeTime;

    @Min(value = 5, message = "Thời gian làm bài tối thiểu 5 phút")
    @Max(value = 180, message = "Thời gian làm bài tối đa 180 phút")
    private int timeLimit;

    @Min(value = 1, message = "Số lần làm tối thiểu là 1")
    @Max(value = 10, message = "Số lần làm tối đa là 10")
    private int attemptsAllowed;

    private boolean allowReview = true;
    private boolean published = false;
}
