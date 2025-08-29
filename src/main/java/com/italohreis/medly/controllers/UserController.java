package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.user.UserPasswordUpdateDTO;
import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import com.italohreis.medly.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get my user profile",
            description = "Retrieves the profile information of the currently authenticated user."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user is not authenticated).")
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserProfile(authentication));
    }

    @Operation(
            summary = "Change my password",
            description = "Allows the currently authenticated user to change their password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., password does not meet criteria)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user is not authenticated).")
    })
    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changeMyPassword(Authentication authentication,
                                 @RequestBody @Valid UserPasswordUpdateDTO dto) {
        userService.changeUserPassword(authentication, dto);
    }
}
