package com.dophuong.quiz_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizExportResponse {
    private String fileName;
    private String filePath;
}
