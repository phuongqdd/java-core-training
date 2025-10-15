package com.dophuong.course_service.dto.response;

import com.dophuong.course_service.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddUserToCourseResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseName;
    private Role role;
    private Boolean isOwner;
    private LocalDateTime enrolledAt;
}
