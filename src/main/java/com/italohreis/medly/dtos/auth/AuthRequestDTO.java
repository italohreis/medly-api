package com.italohreis.medly.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data transfer object for user authentication request")
public record AuthRequestDTO(
        @Schema(description = "User's email address.", example = "user@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "User's password.", example = "password123", minLength = 6, maxLength = 100, required = true)
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100)
        String password
) {}