package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CRM is required")
        String crm,

        @NotNull(message = "Specialty is required")
        Specialty specialty,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
