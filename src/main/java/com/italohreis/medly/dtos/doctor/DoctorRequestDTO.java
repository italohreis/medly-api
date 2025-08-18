package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Speciality;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CRM is required")
        String crm,

        @NotNull(message = "Specialty is required")
        Speciality specialty,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
