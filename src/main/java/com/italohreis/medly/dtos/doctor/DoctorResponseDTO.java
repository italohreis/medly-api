package com.italohreis.medly.dtos.doctor;

import java.util.UUID;

public record DoctorResponseDTO(
        UUID id,
        String name,
        String crm,
        String specialty,
        String email
) {}
