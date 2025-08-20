package com.italohreis.medly.dtos.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
    @NotNull(message = "Availability ID is required")
    UUID availabilityId,

    @NotNull(message = "Patient ID is required")
    UUID patientId
) {}
