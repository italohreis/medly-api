package com.italohreis.medly.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for user authentication response")
public record AuthResponseDTO(
        @Schema(description = "JWT authentication token.", example = "eyJhbGciOiJIUzUxMiJ9...")
        String token,
        @Schema(description = "Role of the authenticated user.", example = "PATIENT")
        String role
) {}