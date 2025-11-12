package com.italohreis.medly.dtos.user;

import com.italohreis.medly.enums.Role;
import com.italohreis.medly.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Data transfer object for the authenticated user's profile information")
public record UserProfileResponseDTO(
        @Schema(description = "User's unique identifier.")
        UUID userId,
        @Schema(description = "User's full name.")
        String name,
        @Schema(description = "User's email address.")
        String email,
        @Schema(description = "User's role in the system.")
        Role role,
        @Schema(description = "Patient-specific profile details (null if the user is not a patient).")
        PatientProfileDTO patientProfile,
        @Schema(description = "Doctor-specific profile details (null if the user is not a doctor).")
        DoctorProfileDTO doctorProfile
) {
    @Schema(description = "Patient-specific profile data")
    public record PatientProfileDTO(
            @Schema(description = "Patient's unique identifier.")
            UUID patientId,
            @Schema(description = "Patient's CPF number.")
            String cpf,
            @Schema(description = "Patient's birth date.")
            LocalDate birthDate
    ) {}

    @Schema(description = "Doctor-specific profile data")
    public record DoctorProfileDTO(
            @Schema(description = "Doctor's unique identifier.")
            UUID doctorId,
            @Schema(description = "Doctor's CRM number.")
            String crm,
            @Schema(description = "Doctor's medical specialty.")
            Specialty specialty
    ) {}
}