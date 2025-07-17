package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.availability.AvailabilityRequestDTO;
import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.services.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    private final AvailabilityMapper availabilityMapper;

    @PostMapping
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(
            @RequestBody @Valid AvailabilityRequestDTO availabilityRequestDTO) {

        Availability availability = availabilityMapper.toModel(availabilityRequestDTO);
        return ResponseEntity.ok(availabilityService.createAvailability(availability));
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDTO>> getAvailabilitiesByDoctorId(
            @RequestParam("doctorId") UUID doctorId) {

        List<Availability> availabilities = availabilityService.getAvailabilitiesByDoctorId(doctorId);

        List<AvailabilityResponseDTO> responseDTOs = availabilities.stream()
                .map(availabilityMapper::toDto)
                .toList();

        return ResponseEntity.ok(responseDTOs);
    }
}
