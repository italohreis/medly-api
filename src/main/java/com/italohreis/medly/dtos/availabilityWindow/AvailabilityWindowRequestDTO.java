package com.italohreis.medly.dtos.availabilityWindow;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Data transfer object for creating a new availability window for a doctor")
public record AvailabilityWindowRequestDTO(
        @Schema(description = "Unique identifier of the doctor.", required = true)
        @NotNull
        UUID doctorId,

        @Schema(description = "Start time of the availability window (must be in the future).", example = "2025-12-31T09:00:00", required = true)
        @NotNull
        @Future
        LocalDateTime startTime,

        @Schema(description = "End time of the availability window (must be in the future).", example = "2025-12-31T17:00:00", required = true)
        @NotNull
        @Future
        LocalDateTime endTime,

        @Schema(description = "Duration of each time slot in minutes (minimum 15).", example = "30", required = true)
        @NotNull
        @Min(15)
        int slotDurationInMinutes
) {}