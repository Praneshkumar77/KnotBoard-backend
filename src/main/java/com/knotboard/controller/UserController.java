package com.knotboard.controller;

import com.knotboard.dto.RoleUpdateDto;
import com.knotboard.dto.UserDto;
import com.knotboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserDto> changeUserRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        return ResponseEntity.ok(userService.changeUserRole(id, roleUpdateDto.getRole()));
    }

}
