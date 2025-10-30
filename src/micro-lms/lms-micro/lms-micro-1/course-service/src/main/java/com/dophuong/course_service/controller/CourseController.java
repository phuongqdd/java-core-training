package com.dophuong.course_service.controller;

import com.dophuong.course_service.dto.request.AddUserToCourseRequest;
import com.dophuong.course_service.dto.request.CourseCreateRequest;
import com.dophuong.course_service.dto.request.UpdateUserRoleRequest;
import com.dophuong.course_service.dto.response.*;
import com.dophuong.course_service.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @RequestBody @Valid CourseCreateRequest request,
            Authentication authentication){
        CourseResponse response = courseService.createCourse(request, authentication.getName());
        ApiResponse<CourseResponse> response1 = ApiResponse.<CourseResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(response)
                .message("Tạp khóa học thành công!")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response1);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserCourseResponse>>> getAllCourse(){
        return ResponseEntity.ok(ApiResponse.<List<UserCourseResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách khóa học thành công")
                        .data(courseService.getAllCourse())
                        .timestamp(LocalDateTime.now())
                .build());
    }

//    @GetMapping("/{courseId}/users")
//    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isUserInCourse(#courseId)")
//    public ResponseEntity<ApiResponse<List<UserInCourseResponse>>> getAllUsersInCourse(@PathVariable Long courseId) {
//        List<UserInCourseResponse> users = courseService.getAllUsersInCourse(courseId);
//        return ResponseEntity.ok(ApiResponse.<List<UserInCourseResponse>>builder()
//                        .status(HttpStatus.OK.value())
//                        .message("Lấy danh sánh user thành công của khóa học id = " + courseId)
//                        .data(users)
//                        .timestamp(LocalDateTime.now())
//                .build());
//    }
//
//
    // Thêm 1 user vào khóa học
    @PostMapping("/{courseId}/users")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<String>> addUserToCourse(
            @PathVariable Long courseId,
            @RequestBody AddUserToCourseRequest request
            ){
        courseService.addUserToCourse(courseId, request);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(200)
                .message("Thêm user vào khóa học thành công")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{courseId}/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<String>> updateUserRoleInCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequest request) {

        courseService.updateUserRole(courseId, userId, request.getRole());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật quyền thành công cho id = " + userId)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

}
