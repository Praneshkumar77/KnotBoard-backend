package com.knotboard.controller;

import com.knotboard.dto.AuthRequestDto;
import com.knotboard.dto.AuthResponseDto;
import com.knotboard.dto.RegisterRequestDto;
import com.knotboard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AuthResponseDto> registerFirstAdmin(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.registerFirstAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDto> adminLogin(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authService.adminLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contributor/login")
    public ResponseEntity<AuthResponseDto> contributorLogin(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authService.contributorLogin(request);
        return ResponseEntity.ok(response);
    }

}
