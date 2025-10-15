package com.dophuong.identity_service.controller;

import com.dophuong.identity_service.dto.response.RoleResponse;
import com.dophuong.identity_service.dto.response.UserResponse;
import com.dophuong.identity_service.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/role")
public class RoleInternalController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("HiHI");
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable int roleId) {

        return ResponseEntity.ok(roleService.getRole(roleId));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<RoleResponse> getByRoleName(@PathVariable String name) {
        RoleResponse response = roleService.getRoleByName(name);
        return ResponseEntity.ok(response);
    }
}
