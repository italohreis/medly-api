package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Endpoints for managing appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment by linking a patient to an available TimeSlot. " +
                    "Requires appropriate authentication and permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null ID, incorrect format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., the provided patientId or timeSlotId does not exist).")
    })
    @PostMapping
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('PATIENT') and @securityService.isPatientOwner(authentication, #dto.patientId())) or " +
                    "(hasRole('DOCTOR') and @securityService.isDoctorOwnerOfTimeSlot(authentication, #dto.timeSlotId()))"
    )
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody @Valid AppointmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                appointmentService.createAppointment(dto));
    }

    @Operation(
            summary = "List appointments with optional filters",
            description = "Retrieves a paginated list of appointments, optionally filtered by patient ID, doctor ID, and date range. " +
                    "Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., incorrect date format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action).")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAppointments(
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                appointmentService.listAppointments(doctorId, patientId, startDate, endDate, pageable));
    }

    @Operation(
            summary = "Get appointment details by ID",
            description = "Retrieves detailed information about a specific appointment using its ID. " +
                    "Requires appropriate authentication and permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., the provided appointment ID does not exist).")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isUserPartOfAppointment(authentication, #id)")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                appointmentService.getAppointmentById(id));
    }

    @Operation(
            summary = "Cancel an appointment",
            description = "Cancels an existing appointment by its ID. Requires appropriate authentication and permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., the provided appointment ID does not exist)."),
            @ApiResponse(responseCode = "400", description = "Invalid operation (e.g., trying to cancel an already completed appointment).")
    })
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isUserPartOfAppointment(authentication, #id)")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                appointmentService.cancelAppointment(id)
        );
    }

    @Operation(
            summary = "Complete an appointment",
            description = "Marks an existing appointment as completed by its ID. Requires appropriate authentication and permissions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment completed successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., the provided appointment ID does not exist)."),
            @ApiResponse(responseCode = "400", description = "Invalid operation (e.g., trying to complete an already cancelled or completed appointment).")
    })
    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwnerOfAppointment(authentication, #id)")
    public ResponseEntity<AppointmentResponseDTO> completeAppointment(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                appointmentService.completeAppointment(id)
        );
    }

    @Operation(
            summary = "Delete an appointment",
            description = "Deletes an existing appointment by its ID. Only administrators can perform this action."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., the provided appointment ID does not exist)."),
            @ApiResponse(responseCode = "400", description = "Invalid operation (e.g., trying to delete an already completed appointment).")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }
}
