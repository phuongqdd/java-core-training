package com.dophuong.identity_service.controller;

import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
public class UserInternalController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo(){
        return ResponseEntity.ok(userService.getIdInLogin());
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUserName(username);
        return ResponseEntity.ok(response);
    }
}
