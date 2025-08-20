package com.italohreis.medly.dtos.appointment;

import com.italohreis.medly.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        AppointmentStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        DoctorInfo doctor,
        PatientInfo patient
) {
    public record DoctorInfo(UUID id, String name, String crm) {}
    public record PatientInfo(UUID id, String name) {}
}
