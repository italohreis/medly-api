package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import com.italohreis.medly.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserProfile(authentication));
    }
}
