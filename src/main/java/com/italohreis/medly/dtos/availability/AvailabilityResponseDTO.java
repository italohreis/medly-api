package com.italohreis.medly.dtos.availability;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilityResponseDTO(
    UUID id,
    UUID doctorId,
    String doctorName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Boolean isAvailable
) {}
