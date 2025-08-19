package com.italohreis.medly.dtos.availability;

import com.italohreis.medly.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public record AvailabilityUpdateStatusDTO(
        @NotNull(message = "Availability Status is required")
        AvailabilityStatus status
) {}
