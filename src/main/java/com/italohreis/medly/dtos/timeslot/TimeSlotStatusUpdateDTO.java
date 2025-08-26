package com.italohreis.medly.dtos.timeslot;

import com.italohreis.medly.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public record TimeSlotStatusUpdateDTO(
    @NotNull
    AvailabilityStatus status
) {
}
