package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizCreateRequest {
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

    // Tổng số câu hỏi
    @Min(value = 10, message = "Tổng số câu hỏi tối thiểu là 10")
    @Max(value = 60, message = "Tổng số câu hỏi tối đa là 60")
    private Integer totalQuestions;

    @Min(value = 1, message = "Số lần làm tối thiểu là 1")
    @Max(value = 10, message = "Số lần làm tối đa là 10")
    private int attemptsAllowed;

    private boolean allowReview = true;
    private boolean isPublished = false;

    private int pctRl;
    private int pctUn;
    private int pctAp;
    private int pctAn;

}
