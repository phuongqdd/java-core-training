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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<ApiResponse<CourseCreateResponse>> createCourse(
            @RequestBody @Valid CourseCreateRequest request,
            Authentication authentication){
        CourseCreateResponse response = courseService.createCourse(request, authentication.getName());
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
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isUserInCourse(#courseId)")
    public ResponseEntity<ApiResponse<List<UserInCourseResponse>>> getAllUsersInCourse(@PathVariable Long courseId) {
        List<UserInCourseResponse> users = courseService.getAllUsersInCourse(courseId);
        return ResponseEntity.ok(ApiResponse.<List<UserInCourseResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sánh user thành công của khóa học id = " + courseId)
                        .data(users)
                        .timestamp(LocalDateTime.now())
                .build());
    }


    // Thêm 1 user vào khóa học
    @PostMapping("/{courseId}/users")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<AddUserToCourseResponse>> addUserToCourse(
            @PathVariable Long courseId,
            @RequestBody AddUserToCourseRequest request
            ){
        AddUserToCourseResponse response = courseService.addUserToCourse(courseId, request);

        ApiResponse<AddUserToCourseResponse> apiResponse = ApiResponse.<AddUserToCourseResponse>builder()
                .status(200)
                .message("Thêm user vào khóa học thành công")
                .timestamp(LocalDateTime.now())
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
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
