package com.italohreis.medly.dtos.doctor;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Data transfer object for doctor response details")
public record DoctorResponseDTO(
        @Schema(description = "Unique identifier of the doctor.")
        UUID id,
        @Schema(description = "Doctor's full name.")
        String name,
        @Schema(description = "Doctor's CRM number.")
        String crm,
        @Schema(description = "Doctor's medical specialty.")
        String specialty,
        @Schema(description = "Doctor's email address.")
        String email
) {}