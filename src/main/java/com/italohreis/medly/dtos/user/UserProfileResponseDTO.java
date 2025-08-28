package com.italohreis.medly.dtos.user;

import com.italohreis.medly.enums.Role;
import com.italohreis.medly.enums.Specialty;
import java.time.LocalDate;
import java.util.UUID;

public record UserProfileResponseDTO(
        UUID userId,
        String name,
        String email,
        Role role,
        PatientProfileDTO patientProfile,
        DoctorProfileDTO doctorProfile
) {
    public record PatientProfileDTO(
            UUID patientId,
            String cpf,
            LocalDate birthDate
    ) {}

    public record DoctorProfileDTO(
            UUID doctorId,
            String crm,
            Specialty specialty
    ) {}
}