package com.dophuong.lms.learning_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizExportResponse {
    private String fileName;
    private String filePath;
}
