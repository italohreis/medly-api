package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.dtos.patient.PatientUpdateDTO;
import com.italohreis.medly.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isPatientOwner(authentication, #id)")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable("id") UUID id,
                                                            @RequestBody @Valid PatientUpdateDTO patientUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatient(id, patientUpdateDTO));
    }
}
