package com.dophuong.course_service.dto.request;

import lombok.Data;

@Data
public class AddUserToCourseRequest {
    private Long userId;
    private String role;
}
