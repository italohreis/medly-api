package com.italohreis.medly.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateDTO(
        @NotBlank
        String currentPassword,

        @NotBlank @Size(min = 6, max = 100)
        String newPassword,

        @NotBlank @Size(min = 6, max = 100)
        String confirmPassword
) {}