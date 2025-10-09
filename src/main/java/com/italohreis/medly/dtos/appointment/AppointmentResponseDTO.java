package com.italohreis.medly.dtos.appointment;

import com.italohreis.medly.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Data transfer object for appointment response details")
public record AppointmentResponseDTO(
        @Schema(description = "Unique identifier of the appointment.")
        UUID id,
        @Schema(description = "Current status of the appointment.")
        AppointmentStatus status,
        @Schema(description = "Start time of the appointment.")
        LocalDateTime startTime,
        @Schema(description = "End time of the appointment.")
        LocalDateTime endTime,
        @Schema(description = "Information about the doctor associated with the appointment.")
        DoctorInfo doctor,
        @Schema(description = "Information about the patient associated with the appointment.")
        PatientInfo patient
) {
    @Schema(description = "Simplified doctor information for the appointment")
    public record DoctorInfo(
            @Schema(description = "Doctor's unique identifier.")
            UUID id,
            @Schema(description = "Doctor's full name.")
            String name,
            @Schema(description = "Doctor's CRM number.")
            String crm
    ) {}

    @Schema(description = "Simplified patient information for the appointment")
    public record PatientInfo(
            @Schema(description = "Patient's unique identifier.")
            UUID id,
            @Schema(description = "Patient's full name.")
            String name
    ) {}
}