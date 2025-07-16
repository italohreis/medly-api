package com.italohreis.medly.dtos.Availability;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilityResponseDTO(
    UUID id,
    UUID doctorId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    boolean isAvailable
) {}
