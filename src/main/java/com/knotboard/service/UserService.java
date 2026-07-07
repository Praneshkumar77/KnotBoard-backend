package com.knotboard.service;

import com.knotboard.dto.UserDto;
import com.knotboard.entity.AppUser;
import com.knotboard.entity.Role;
import com.knotboard.exception.ResourceNotFoundException;
import com.knotboard.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<UserDto> getAllUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public UserDto changeUserRole(Long userId, String newRole) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role;
        try {
            role = Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        appUser.setRole(role);
        AppUser updated = appUserRepository.save(appUser);
        return mapToDto(updated);
    }

    public long countUsers() {
        return appUserRepository.count();
    }

    private UserDto mapToDto(AppUser appUser) {
        return new UserDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getEmail(),
                appUser.getRole().name()
        );
    }

}
