package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.auth.AuthRequestDTO;
import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.dtos.auth.DoctorRegisterDTO;
import com.italohreis.medly.dtos.auth.PatientRegisterDTO;
import com.italohreis.medly.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register-patient")
    public ResponseEntity<AuthResponseDTO> registerPatient(@RequestBody @Valid PatientRegisterDTO patientRegisterDTO) {
        AuthResponseDTO response = authService.registerPatient(patientRegisterDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register-doctor")
    public ResponseEntity<AuthResponseDTO> registerDoctor(@RequestBody @Valid DoctorRegisterDTO doctorRegisterDTO) {
        AuthResponseDTO response = authService.registerDoctor(doctorRegisterDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO authRequestDTO) {
        AuthResponseDTO response = authService.login(authRequestDTO);
        return ResponseEntity.ok(response);
    }

}
