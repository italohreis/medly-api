package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.availability.AvailabilityRequestDTO;
import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.dtos.availability.AvailabilityUpdateStatusDTO;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.services.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    private final AvailabilityMapper availabilityMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #availabilityRequestDTO.doctorId)")
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(
            @RequestBody @Valid AvailabilityRequestDTO availabilityRequestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                availabilityService.createAvailability(
                        availabilityMapper.toModel(availabilityRequestDTO)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #doctorId)")
    public ResponseEntity<Page<AvailabilityResponseDTO>> getAvailabilitiesByDoctorId(
            @RequestParam("doctorId") UUID doctorId,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(availabilityService.getAvailabilitiesByDoctorId(doctorId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AvailabilityResponseDTO>> searchAvailabilities(
            @RequestParam(value = "doctorId", required = false) UUID doctorId,
            @RequestParam(value = "speciality", required = false) String speciality,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                availabilityService.searchAvailabilities(doctorId, speciality, startDate, endDate, pageable));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwnerOfAvailability(authentication, #id)")
    public ResponseEntity<AvailabilityResponseDTO> updateAvailabilityStatus(
            @PathVariable UUID id,
            @RequestBody @Valid AvailabilityUpdateStatusDTO availabilityUpdateStatusDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                availabilityService.updateAvailabilityStatus(id, availabilityUpdateStatusDTO));
    }
}
