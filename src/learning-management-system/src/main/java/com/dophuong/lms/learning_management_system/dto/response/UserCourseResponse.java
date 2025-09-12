package com.dophuong.lms.learning_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseResponse {
    private Long courseId;
    private String name;          // thay vì title
    private String description;
    private String thumbnailUrl;  // thay vì thumbnail
    private Integer capacity;
    private Boolean isPublished;
    private LocalDateTime createdAt;

    // Thông tin tham gia của user
    private LocalDateTime enrolledAt;
    private String role;
    private Boolean isOwner;
}
