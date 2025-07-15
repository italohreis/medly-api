package com.italohreis.medly.dtos.appointment;

import com.italohreis.medly.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        LocalDateTime date,
        AppointmentStatus status,
        UUID doctorId,
        String doctorName,
        UUID patientId,
        String patientName
) {}
