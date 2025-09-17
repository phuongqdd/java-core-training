package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CourseCreateResponse {
    private Long id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private Integer capacity;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String owner;
}
