package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.dtos.doctor.DoctorUpdateDTO;
import com.italohreis.medly.services.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Endpoints for managing doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(
            summary = "Create a new doctor",
            description = "Creates a new doctor. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have ADMIN role)."),
            @ApiResponse(responseCode = "400", description =  "Conflict (e.g., email or CRM already in use).")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody @Valid DoctorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(dto));
    }

    @Operation(
            summary = "Update an existing doctor",
            description = "Updates an existing doctor's details. Requires ADMIN role or ownership by the doctor."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #id)")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("id") UUID id,
                                                          @RequestBody @Valid DoctorUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.updateDoctor(id, dto));
    }

    @Operation(
            summary = "Get a paginated list of doctors with optional filters",
            description = "Retrieves a paginated list of doctors, optionally filtered by name, specialty, or CRM. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., incorrect filter format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<DoctorResponseDTO>> getDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String crm,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctors(name, specialty, crm, pageable));
    }

    @Operation(
            summary = "Get doctor details by ID",
            description = "Retrieves detailed information about a specific doctor by their ID. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctorById(id));
    }

    @Operation(
            summary = "Delete a doctor by ID",
            description = "Deletes a doctor by their ID. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty ID)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have ADMIN role)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
    }
}
