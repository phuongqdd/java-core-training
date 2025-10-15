package com.dophuong.course_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CourseResponse {
    private Long id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private Integer capacity;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
