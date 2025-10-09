package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object for creating a new doctor")
public record DoctorRequestDTO(
        @Schema(description = "Doctor's full name.", example = "Dr. John Doe", required = true)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Doctor's CRM number.", example = "12345-SP", required = true)
        @NotBlank(message = "CRM is required")
        String crm,

        @Schema(description = "Doctor's medical specialty.", required = true)
        @NotNull(message = "Specialty is required")
        Specialty specialty,

        @Schema(description = "Doctor's email address.", example = "doctor.doe@medly.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "Doctor's password for login.", example = "strongPassword!123", required = true)
        @NotBlank(message = "Password is required")
        String password
) {}