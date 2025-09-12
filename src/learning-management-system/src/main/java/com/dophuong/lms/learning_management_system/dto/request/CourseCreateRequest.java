package com.dophuong.lms.learning_management_system.dto.request;

import lombok.Data;

@Data
public class CourseCreateRequest {
    private String name;
    private String description;
    private String thumbnailUrl;
    private Integer capacity;
    private Boolean isPublished;
    private Long ownerId;
}
