package com.italohreis.medly.controllers.docs;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.dtos.doctor.DoctorUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Doctors", description = "Endpoints for managing doctors")
public interface DoctorControllerDocs {

    @Operation(
            summary = "Create a new doctor",
            description = "Creates a new doctor. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have ADMIN role)."),
            @ApiResponse(responseCode = "409", description =  "Conflict (e.g., email or CRM already in use).")
    })
    ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody @Valid DoctorRequestDTO dto);

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
    ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("id") UUID id,
                                                   @RequestBody @Valid DoctorUpdateDTO dto);

    @Operation(
            summary = "Get a paginated list of doctors with optional filters",
            description = "Retrieves a paginated list of doctors, optionally filtered by name, specialty, or CRM. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., incorrect filter format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
    })
    ResponseEntity<Page<DoctorResponseDTO>> getDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String crm,
            Pageable pageable);

    @Operation(
            summary = "Get doctor details by ID",
            description = "Retrieves detailed information about a specific doctor by their ID. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID.")
    })
    ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id);

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
    void deleteDoctor(@PathVariable UUID id);
}