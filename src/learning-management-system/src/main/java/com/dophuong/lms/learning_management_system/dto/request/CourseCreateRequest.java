package com.dophuong.lms.learning_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseCreateRequest {
    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(max = 100, message = "Tên khóa học tối đa 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @Size(max = 255, message = "Đường dẫn thumbnail tối đa 255 ký tự")
//    @Pattern(
//            regexp = "^(http(s)?://.*)?$",
//            message = "Thumbnail URL phải bắt đầu bằng http hoặc https nếu có"
//    )
    private String thumbnailUrl;

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa phải ít nhất là 1")
    private Integer capacity;

    @NotNull(message = "Trạng thái xuất bản không được để trống")
    private Boolean isPublished;
}
