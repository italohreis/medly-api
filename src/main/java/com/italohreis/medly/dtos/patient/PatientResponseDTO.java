package com.italohreis.medly.dtos.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Data transfer object for patient response details")
public record PatientResponseDTO(
        @Schema(description = "Unique identifier of the patient.")
        UUID id,
        @Schema(description = "Patient's full name.")
        String name,
        @Schema(description = "Patient's email address.")
        String email,
        @Schema(description = "Patient's CPF number.")
        String cpf,
        @Schema(description = "Patient's birth date.")
        LocalDate birthDate
) {}