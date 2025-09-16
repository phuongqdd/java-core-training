package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.RoleRequest;
import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.RoleResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.service.AdminService;
import com.dophuong.lms.learning_management_system.service.RoleService;
import jakarta.validation.Valid;
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
    @Autowired
    private RoleService roleService;

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

    @GetMapping("/role/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable int id){
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                        .status(200)
                        .message("Lấy thông tin role thành công")
                        .data(roleService.getRole(id))
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/role")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo role mới thành công")
                        .data(roleService.createRole(request))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/role")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles(){
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách role thành công")
                        .data(roleService.getAllRoles())
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable int id,
            @RequestBody @Valid RoleRequest request) {

        RoleResponse updated = roleService.updateRole(id, request);

        return ResponseEntity.ok(
                ApiResponse.<RoleResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật role thành công")
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable int id) {

        roleService.deleteRole(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Xóa role thành công")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/student/createStudent")
    public ResponseEntity<ApiResponse<UserResponse>> createUserStudent(@RequestBody UserCreateRequest request){
        UserResponse userResponse = adminService.createUser(request, Role.STUDENT.name());
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
        UserResponse userResponse = adminService.createUser(request, Role.INSTRUCTOR.name());
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
