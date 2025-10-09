package com.italohreis.medly.dtos.timeslot;

import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Data transfer object for time slot response")
public record TimeSlotResponseDTO(
        @Schema(description = "Unique identifier of the time slot.")
        UUID id,
        @Schema(description = "Start time of the time slot.")
        LocalDateTime startTime,
        @Schema(description = "End time of the time slot.")
        LocalDateTime endTime,
        @Schema(description = "Current availability status of the time slot.")
        AvailabilityStatus status,
        @Schema(description = "Information about the doctor associated with this time slot.")
        DoctorInfo doctor
) {
    @Schema(description = "Simplified doctor information for a time slot")
    public record DoctorInfo(
            @Schema(description = "Doctor's unique identifier.")
            UUID id,
            @Schema(description = "Doctor's full name.")
            String name,
            @Schema(description = "Doctor's medical specialty.")
            Specialty specialty
    ) {}
}