package com.italohreis.medly.dtos.availabilityWindow;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilityWindowRequestDTO(
    @NotNull
    UUID doctorId,

    @NotNull
    @Future
    LocalDateTime startTime,

    @NotNull
    @Future
    LocalDateTime endTime,

    @NotNull
    @Min(15)
    int slotDurationInMinutes
) {}
