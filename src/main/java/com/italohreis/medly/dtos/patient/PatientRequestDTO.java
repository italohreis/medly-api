package com.italohreis.medly.dtos.patient;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PatientRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "CPF is required")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
        String cpf,

        @NotNull(message = "Birth date is required")
        @Past
        LocalDate birthDate
) {}
