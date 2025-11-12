package com.italohreis.medly.dtos.timeslot;

import com.italohreis.medly.enums.AvailabilityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object for updating the status of a time slot")
public record TimeSlotStatusUpdateDTO(
        @Schema(description = "The new status for the time slot. Can only be AVAILABLE or BLOCKED.", example = "BLOCKED", required = true)
        @NotNull
        AvailabilityStatus status
) {}