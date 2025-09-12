package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<String>> unlockUser(@PathVariable Long id){
        adminService.unLockUser(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                        .status(200)
                        .message("Thực hiện thành công")
                        .data("Mở khóa thành công user có id = " + id)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/student/createStudent")
    public ResponseEntity<ApiResponse<UserResponse>> createUserStudent(@RequestBody UserCreateRequest request){
        UserResponse userResponse = adminService.createUser(request, Role.STUDENT);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo tài khoản thành công")
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/student/{id}")
    public  ResponseEntity<ApiResponse<UserResponse>> getStudent(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy student thành công với id = " + id)
                .data(adminService.getStudent(id))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/student/getAllStudent")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllStudent(){
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách student thành công")
                .timestamp(LocalDateTime.now())
                .data(adminService.getAllStudents())
                .build());
    }

    @PostMapping("/instructor/createInstructor")
    public ResponseEntity<ApiResponse<UserResponse>> createUserInstructor(@RequestBody UserCreateRequest request){
        UserResponse userResponse = adminService.createUser(request, Role.INSTRUCTOR);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo tài khoản thành công!")
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/instructor/{id}")
    public  ResponseEntity<ApiResponse<UserResponse>> getInstructor(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy instructor thành công với id = " + id)
                .data(adminService.getInstructor(id))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/instructor")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllInstructors(){
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách instructor thành công")
                .data(adminService.getAllInstructors())
                .timestamp(LocalDateTime.now())
                .build());
    }

}
