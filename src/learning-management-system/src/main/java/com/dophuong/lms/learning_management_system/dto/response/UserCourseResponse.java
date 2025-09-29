package com.dophuong.lms.learning_management_system.dto.response;

import com.dophuong.lms.learning_management_system.enums.Role;
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
    private String courseName;
    private String description;
    private String thumbnailUrl;

    // Thông tin tham gia của user
    private LocalDateTime enrolledAt;
    private Role roleName;
    private Boolean isOwner;
}
