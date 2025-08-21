package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.services.AppointmentService;
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
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('PATIENT') and @securityService.isPatientOwner(authentication, #appointmentRequestDTO.patientId())) or " +
                    "(hasRole('DOCTOR') and @securityService.isDoctorOwnerOfAvailability(authentication, #appointmentRequestDTO.availabilityId()))"
    )
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody @Valid AppointmentRequestDTO appointmentRequestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                appointmentService.createAppointment(appointmentRequestDTO));
    }

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
}
