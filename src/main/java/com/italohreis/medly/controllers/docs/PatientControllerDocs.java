package com.italohreis.medly.controllers.docs;

import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.dtos.patient.PatientUpdateDTO;
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

@Tag(name = "Patients", description = "Endpoints for managing patients")
public interface PatientControllerDocs {

    @Operation(
            summary = "Create a new patient",
            description = "Creates a new patient. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., email already in use).")
    })
    ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Valid PatientRequestDTO dto);

    @Operation(
            summary = "Update an existing patient",
            description = "Updates an existing patient's details. Requires ADMIN role or ownership by the patient."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Patient not found with the provided ID.")
    })
    ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable("id") UUID id,
                                                     @RequestBody @Valid PatientUpdateDTO dto);

    @Operation(
            summary = "List patients with optional filters",
            description = "Retrieves a paginated list of patients, optionally filtered by name, CPF, and email. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., incorrect format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have ADMIN role).")
    })
    ResponseEntity<Page<PatientResponseDTO>> getPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            Pageable pageable);

    @Operation(
            summary = "Get patient details by ID",
            description = "Retrieves patient details by ID. Requires ADMIN role or ownership by the patient."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have permission for this action)."),
            @ApiResponse(responseCode = "404", description = "Patient not found with the provided ID.")
    })
    ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id);

    @Operation(
            summary = "Delete a patient by ID",
            description = "Deletes a patient by ID. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Authentication failure (token is invalid or expired)."),
            @ApiResponse(responseCode = "403", description = "Access denied (user does not have ADMIN role)."),
            @ApiResponse(responseCode = "404", description = "Patient not found with the provided ID.")
    })
    void deletePatient(@PathVariable UUID id);
}