package com.italohreis.medly.dtos.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Data transfer object for creating a new patient")
public record PatientRequestDTO(
        @Schema(description = "Patient's full name.", example = "John Smith", required = true)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Patient's email address.", example = "john.smith@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "Patient's password for login.", example = "mySecurePassword123", required = true)
        @NotBlank(message = "Password is required")
        String password,

        @Schema(description = "Patient's CPF number in format XXX.XXX.XXX-XX.", example = "123.456.789-00", required = true)
        @NotBlank(message = "CPF is required")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
        String cpf,

        @Schema(description = "Patient's birth date (must be in the past).", example = "1990-01-15", required = true)
        @NotNull(message = "Birth date is required")
        @Past
        LocalDate birthDate
) {}