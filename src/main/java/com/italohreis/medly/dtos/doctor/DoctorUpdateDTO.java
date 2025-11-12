package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Data transfer object for updating an existing doctor's details. All fields are optional.")
public record DoctorUpdateDTO(
        @Schema(description = "Doctor's new full name.", example = "Dr. Jane Doe")
        String name,

        @Schema(description = "Doctor's new email address.", example = "jane.doe@medly.com")
        @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @Schema(description = "Doctor's new medical specialty.", example = "CARDIOLOGY")
        Specialty specialty
) {}