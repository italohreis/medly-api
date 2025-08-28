package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.user.UserPasswordUpdateDTO;
import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import com.italohreis.medly.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changeMyPassword(Authentication authentication,
                                 @RequestBody @Valid UserPasswordUpdateDTO dto) {
        userService.changeUserPassword(authentication, dto);
    }
}
