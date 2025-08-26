package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowRequestDTO;
import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotStatusUpdateDTO;
import com.italohreis.medly.services.AvailabilityWindowService;
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
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final AvailabilityWindowService availabilityWindowService;

    @PostMapping("/windows")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #dto.doctorId())")
    public ResponseEntity<AvailabilityWindowResponseDTO> createAvailabilityWindow(
            @RequestBody @Valid AvailabilityWindowRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                availabilityWindowService.createAvailabilityWindow(dto));
    }

    @GetMapping("/windows")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #doctorId)")
    public ResponseEntity<Page<AvailabilityWindowResponseDTO>> getDoctorSchedule(
            @RequestParam("doctorId") UUID doctorId,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                availabilityWindowService.getDoctorSchedule(doctorId, pageable));
    }

    @GetMapping("/timeslots/search")
    public ResponseEntity<Page<TimeSlotResponseDTO>> searchAvailableTimeSlots(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) String specialty,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                availabilityWindowService.searchAvailableTimeSlots(doctorId, specialty, startDate, endDate, pageable));
    }

    @PatchMapping("/timeslots/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwnerOfTimeSlot(authentication, #id)")
    public ResponseEntity<TimeSlotResponseDTO> updateTimeSlotStatus(
            @PathVariable UUID id,
            @RequestBody @Valid TimeSlotStatusUpdateDTO dto) {

        return ResponseEntity.status(HttpStatus.OK).body(
                availabilityWindowService.updateTimeSlotStatus(id, dto));
    }
}