package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.UserCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.ApiResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserResponse;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

}
