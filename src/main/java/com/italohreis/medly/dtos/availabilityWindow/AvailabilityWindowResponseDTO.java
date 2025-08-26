package com.italohreis.medly.dtos.availabilityWindow;

import com.italohreis.medly.enums.AvailabilityStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AvailabilityWindowResponseDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        DoctorInfo doctor,
        List<TimeSlotDTO> timeSlots
) {
    public record DoctorInfo(UUID id, String name) {}

    public record TimeSlotDTO(
            UUID id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            AvailabilityStatus status
    ) {}
}
