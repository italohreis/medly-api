package com.italohreis.medly.dtos.availabilityWindow;

import com.italohreis.medly.enums.AvailabilityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data transfer object for availability window response")
public record AvailabilityWindowResponseDTO(
        @Schema(description = "Unique identifier of the availability window.")
        UUID id,
        @Schema(description = "Start time of the availability window.")
        LocalDateTime startTime,
        @Schema(description = "End time of the availability window.")
        LocalDateTime endTime,
        @Schema(description = "Information about the doctor who owns the window.")
        DoctorInfo doctor,
        @Schema(description = "List of time slots generated within this window.")
        List<TimeSlotDTO> timeSlots
) {
    @Schema(description = "Simplified doctor information")
    public record DoctorInfo(
            @Schema(description = "Doctor's unique identifier.")
            UUID id,
            @Schema(description = "Doctor's full name.")
            String name
    ) {}

    @Schema(description = "Detailed information about a time slot within the window")
    public record TimeSlotDTO(
            @Schema(description = "Unique identifier of the time slot.")
            UUID id,
            @Schema(description = "Start time of the time slot.")
            LocalDateTime startTime,
            @Schema(description = "End time of the time slot.")
            LocalDateTime endTime,
            @Schema(description = "Current availability status of the time slot.")
            AvailabilityStatus status
    ) {}
}