package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.AddUserToCourseRequest;
import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.request.UpdateUserRoleRequest;
import com.dophuong.lms.learning_management_system.dto.response.*;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseCreateResponse>> createCourse(@RequestBody @Valid CourseCreateRequest request){
        CourseCreateResponse response = courseService.createCourse(request);
        ApiResponse<CourseCreateResponse> response1 = ApiResponse.<CourseCreateResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(response)
                .message("Tạp khóa học thành công!")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<UserCourseResponse>>> getAllCourseById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.<List<UserCourseResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách khóa học cho id = " + id + " thành công")
                        .data(courseService.getAllCourseById(id))
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{courseId}/users")
    public ResponseEntity<ApiResponse<List<UserInCourseResponse>>> getAllUsersInCourse(@PathVariable Long courseId) {
        List<UserInCourseResponse> users = courseService.getAllUsersInCourse(courseId);
        return ResponseEntity.ok(ApiResponse.<List<UserInCourseResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sánh user thành công của khóa học id = " + courseId)
                        .data(users)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{courseId}/users")
    public ResponseEntity<ApiResponse<String>> addUserToCourse(
            @PathVariable Long courseId,
            @RequestBody AddUserToCourseRequest request
            ){
        courseService.addUserToCourse(
                courseId,
                request.getUserId(),
                request.getRole()
        );
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Thêm user " + request.getUserId()
                                + " vào khóa học " + courseId + " thành công")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{courseId}/users/{userId}/role")
    public ResponseEntity<ApiResponse<String>> updateUserRoleInCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId,
            @RequestBody UpdateUserRoleRequest request) {

        courseService.updateUserRole(courseId, userId, request.getRole());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật quyền thành công cho id = " + userId)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

}
