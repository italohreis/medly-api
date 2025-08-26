package com.italohreis.medly.dtos.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
    @NotNull(message = "Time Slot ID is required")
    UUID timeSlotId,

    @NotNull(message = "Patient ID is required")
    UUID patientId
) {}
