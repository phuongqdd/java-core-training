package com.dophuong.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    @NotBlank(message = "Tên là trường bắt buộc")
    private String name;
    @NotBlank(message = "Mô tả là trường bắt buộc")
    private String description;
}
