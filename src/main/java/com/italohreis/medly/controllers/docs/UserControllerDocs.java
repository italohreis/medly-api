package com.italohreis.medly.controllers.docs;

import com.italohreis.medly.dtos.user.UserPasswordUpdateDTO;
import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Users", description = "Endpoints for managing users")
public interface UserControllerDocs {

    @Operation(
            summary = "Get my user profile",
            description = "Retrieves the profile information of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user is not authenticated).")
    })
    ResponseEntity<UserProfileResponseDTO> getMyProfile(Authentication authentication);

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
    void changeMyPassword(Authentication authentication,
                          @RequestBody @Valid UserPasswordUpdateDTO dto);
}