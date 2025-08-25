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

    // Apenas uma injeção de serviço, como você decidiu.
    private final AvailabilityWindowService availabilityWindowService;

    // === Endpoints para Médicos/Admins Gerenciarem a Agenda ===

    @PostMapping("/windows")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #dto.doctorId())")
    public ResponseEntity<AvailabilityWindowResponseDTO> createAvailabilityWindow(
            @RequestBody @Valid AvailabilityWindowRequestDTO dto) {

        AvailabilityWindowResponseDTO response = availabilityWindowService.createAvailabilityWindow(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/windows")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #doctorId)")
    public ResponseEntity<Page<AvailabilityWindowResponseDTO>> getDoctorSchedule(
            @RequestParam("doctorId") UUID doctorId,
            Pageable pageable) {

        Page<AvailabilityWindowResponseDTO> schedule = availabilityWindowService.getDoctorSchedule(doctorId, pageable);
        return ResponseEntity.ok(schedule);
    }

    // === Endpoints para Pacientes Buscarem Horários e Admins/Médicos Gerenciarem Slots ===

    @GetMapping("/timeslots/search")
    public ResponseEntity<Page<TimeSlotResponseDTO>> searchAvailableTimeSlots(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) String specialty,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        Page<TimeSlotResponseDTO> timeSlots = availabilityWindowService.searchAvailableTimeSlots(
                doctorId, specialty, startDate, endDate, pageable);
        return ResponseEntity.ok(timeSlots);
    }

    @PatchMapping("/timeslots/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwnerOfTimeSlot(authentication, #id)")
    public ResponseEntity<TimeSlotResponseDTO> updateTimeSlotStatus(
            @PathVariable UUID id,
            @RequestBody @Valid TimeSlotStatusUpdateDTO dto) {

        TimeSlotResponseDTO updatedTimeSlot = availabilityWindowService.updateTimeSlotStatus(id, dto);
        return ResponseEntity.ok(updatedTimeSlot);
    }
}