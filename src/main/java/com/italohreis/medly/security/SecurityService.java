package com.italohreis.medly.security;

import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final DoctorRepository doctorRepository;

    public boolean isDoctorOwner(Authentication authentication, UUID doctorId) {
        User loggedInUser = (User) authentication.getPrincipal();
        return doctorRepository.findById(doctorId)
                .map(doctor -> doctor.getUser().getId().equals(loggedInUser.getId()))
                .orElse(false);
    }

    public boolean isPatientOwner(Authentication authentication, UUID patientId) {
        User loggedInUser = (User) authentication.getPrincipal();
        return doctorRepository.findById(patientId)
                .map(patient -> patient.getUser().getId().equals(loggedInUser.getId()))
                .orElse(false);
    }
}
