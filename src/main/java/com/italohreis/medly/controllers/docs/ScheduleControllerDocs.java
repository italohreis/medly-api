package com.italohreis.medly.controllers.docs;

import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowRequestDTO;
import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotStatusUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Schedule", description = "Endpoints for managing doctors availability and time slots")
public interface ScheduleControllerDocs {

    @Operation(
            summary = "Create a new availability window",
            description = "Creates a new availability window for a doctor. Requires ADMIN role or ownership by the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Availability window created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields, endTime before startTime)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID."),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., overlapping availability windows).")
    })
    ResponseEntity<AvailabilityWindowResponseDTO> createAvailabilityWindow(
            @RequestBody @Valid AvailabilityWindowRequestDTO dto);

    @Operation(
            summary = "Get a doctor's schedule",
            description = "Retrieves a paginated list of availability windows and their associated time slots for a specific doctor. " +
                    "Requires ADMIN role or ownership by the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty doctorId)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID.")
    })
    ResponseEntity<Page<AvailabilityWindowResponseDTO>> getDoctorSchedule(
            @RequestParam("doctorId") UUID doctorId,
            Pageable pageable);

    @Operation(
            summary = "Search for available time slots",
            description = "Searches for available time slots based on optional filters such as doctor ID, specialty, and date range. " +
                    "Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available time slots retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., incorrect date format, endDate before startDate)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action).")
    })
    ResponseEntity<Page<TimeSlotResponseDTO>> searchAvailableTimeSlots(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) String specialty,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable);

    @Operation(
            summary = "Update the status of a time slot",
            description = "Updates the status of a specific time slot. Requires ADMIN role or ownership by the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time slot status updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "400", description = "Invalid status transition (e.g., trying to set a time slot to BOOKED when it's already BOOKED etc.)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Time slot not found with the provided ID."),
    })
    ResponseEntity<TimeSlotResponseDTO> updateTimeSlotStatus(
            @PathVariable UUID id,
            @RequestBody @Valid TimeSlotStatusUpdateDTO dto);

    @Operation(
            summary = "Delete an availability window",
            description = "Deletes an availability window by its ID. Requires ADMIN role or ownership by the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability window deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Availability window not found with the provided ID."),
            @ApiResponse(responseCode = "400", description = "Invalid operation (e.g., trying to delete an availability window that has BOOKED time slots).")
    })
    ResponseEntity<Void> deleteAvailabilityWindow(@PathVariable UUID id);
}