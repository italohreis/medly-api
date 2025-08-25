package com.italohreis.medly.dtos.timeslot;

import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Speciality;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimeSlotResponseDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AvailabilityStatus status,
        DoctorInfo doctor
) {
    public record DoctorInfo(
            UUID id,
            String name,
            Speciality specialty
    ) {}
}
