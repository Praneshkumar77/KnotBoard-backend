package com.knotboard.service;

import com.knotboard.dto.AuthRequestDto;
import com.knotboard.dto.AuthResponseDto;
import com.knotboard.dto.RegisterRequestDto;
import com.knotboard.entity.AppUser;
import com.knotboard.entity.Role;
import com.knotboard.exception.ResourceNotFoundException;
import com.knotboard.repository.AppUserRepository;
import com.knotboard.security.JwtUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AppUserRepository appUserRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        Role role = appUserRepository.count() == 0 ? Role.ADMIN : Role.CONTRIBUTOR;
        return registerWithRole(request, role);
    }

    public AuthResponseDto registerFirstAdmin(RegisterRequestDto request) {
        if (appUserRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalArgumentException("Admin account already exists");
        }
        return registerWithRole(request, Role.ADMIN);
    }

    private AuthResponseDto registerWithRole(RegisterRequestDto request, Role role) {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(role);

        appUserRepository.save(appUser);

        String token = jwtUtil.generateToken(appUser.getUsername());
        return new AuthResponseDto(token, appUser.getUsername(), appUser.getRole().name());
    }

    public AuthResponseDto login(AuthRequestDto request) {
        AppUser appUser = authenticateAndGetUser(request);
        return buildAuthResponse(appUser);
    }

    public AuthResponseDto adminLogin(AuthRequestDto request) {
        AppUser appUser = authenticateAndGetUser(request);
        if (appUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN users can login from the admin login");
        }
        return buildAuthResponse(appUser);
    }

    public AuthResponseDto contributorLogin(AuthRequestDto request) {
        AppUser appUser = authenticateAndGetUser(request);
        if (appUser.getRole() != Role.CONTRIBUTOR) {
            throw new AccessDeniedException("Only CONTRIBUTOR users can login from the contributor login");
        }
        return buildAuthResponse(appUser);
    }

    private AppUser authenticateAndGetUser(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));
    }

    private AuthResponseDto buildAuthResponse(AppUser appUser) {
        String token = jwtUtil.generateToken(appUser.getUsername());
        return new AuthResponseDto(token, appUser.getUsername(), appUser.getRole().name());
    }

}
