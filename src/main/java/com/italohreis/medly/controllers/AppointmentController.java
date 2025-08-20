package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
