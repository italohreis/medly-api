package com.italohreis.medly.controllers.docs;

import com.italohreis.medly.dtos.auth.AuthRequestDTO;
import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
public interface AuthControllerDocs {

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token along with the user's role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, token generated."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty email/password)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (invalid credentials or inactive user)."),
            @ApiResponse(responseCode = "404", description = "User not found with the provided email.")
    })
    ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto);
}