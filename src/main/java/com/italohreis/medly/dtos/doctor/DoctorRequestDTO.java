package com.italohreis.medly.dtos.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CRM is required")
        String crm,

        @NotBlank(message = "Specialty is required")
        String specialty,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
