package com.italohreis.medly.dtos.patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        String name,
        String email,
        String cpf,
        LocalDate birthDate
) {}
