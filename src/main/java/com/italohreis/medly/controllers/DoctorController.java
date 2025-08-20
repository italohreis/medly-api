package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.dtos.doctor.DoctorUpdateDTO;
import com.italohreis.medly.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isDoctorOwner(authentication, #id)")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("id") UUID id,
                                                          @RequestBody @Valid DoctorUpdateDTO doctorUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.updateDoctor(id, doctorUpdateDTO));
    }
}
