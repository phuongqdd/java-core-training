package com.dophuong.lms.learning_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiResponse<T>{
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
