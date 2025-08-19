package com.italohreis.medly.dtos.availability;

import com.italohreis.medly.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilityRequestDTO(
    @NotNull(message = "Doctor ID is required")
    UUID doctorId,

    @NotNull(message = "Start time is required")
    LocalDateTime startTime,

    @NotNull(message = "End time is required")
    LocalDateTime endTime,

    @NotNull(message = "Availability status is required")
    AvailabilityStatus status
) {}
