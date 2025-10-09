package com.italohreis.medly.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data transfer object for updating the user's password")
public record UserPasswordUpdateDTO(
        @Schema(description = "The user's current password.", example = "currentPassword123", required = true)
        @NotBlank
        String currentPassword,

        @Schema(description = "The user's new password.", example = "newStrongPassword!456", minLength = 6, maxLength = 100, required = true)
        @NotBlank @Size(min = 6, max = 100)
        String newPassword,

        @Schema(description = "Confirmation of the new password.", example = "newStrongPassword!456", minLength = 6, maxLength = 100, required = true)
        @NotBlank @Size(min = 6, max = 100)
        String confirmPassword
) {}