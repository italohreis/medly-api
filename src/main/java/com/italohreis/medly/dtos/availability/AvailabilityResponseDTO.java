package com.italohreis.medly.dtos.availability;

import com.italohreis.medly.enums.AvailabilityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvailabilityResponseDTO(
    UUID id,
    UUID doctorId,
    String doctorName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AvailabilityStatus status
) {}
