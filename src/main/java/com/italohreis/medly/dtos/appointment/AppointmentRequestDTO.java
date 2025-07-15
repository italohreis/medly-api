package com.italohreis.medly.dtos.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
    @NotBlank(message = "Doctor ID is required")
    UUID doctorId,

    @NotNull(message = "Appointment date and time is required")
    LocalDateTime dateTime
) {}
