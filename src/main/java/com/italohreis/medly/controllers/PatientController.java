package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.dtos.patient.PatientUpdateDTO;
import com.italohreis.medly.services.PatientService;
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
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Endpoints for managing patients")
public class PatientController {
    private final PatientService patientService;

    @Operation(
            summary = "Create a new patient",
            description = "Creates a new patient. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., null or empty fields)."),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., email already in use).")
    })
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Valid PatientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(dto));
    }

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
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isPatientOwner(authentication, #id)")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable("id") UUID id,
                                                            @RequestBody @Valid PatientUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatient(id, dto));
    }

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
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PatientResponseDTO>> getPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatients(name, cpf, email, pageable));
    }

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
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isPatientOwner(authentication, #id)")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientById(id));
    }

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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
    }
}
