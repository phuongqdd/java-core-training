package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddUserToCourseResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseName;
    private String role;
    private Boolean isOwner;
    private LocalDateTime enrolledAt;
}
