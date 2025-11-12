package com.italohreis.medly.dtos.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Data transfer object for creating a new appointment")
public record AppointmentRequestDTO(
        @Schema(description = "Unique identifier of the time slot to be booked.", required = true)
        @NotNull(message = "Time Slot ID is required")
        UUID timeSlotId,

        @Schema(description = "Unique identifier of the patient for whom the appointment is being booked.", required = true)
        @NotNull(message = "Patient ID is required")
        UUID patientId
) {}