package com.italohreis.medly.security;

import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.PatientRepository;
import com.italohreis.medly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public boolean isDoctorOwner(Authentication authentication, UUID doctorId) {
        String userEmail = (String) authentication.getPrincipal();

        User loggedInUser = userRepository.findByEmail(userEmail)
                .orElse(null);

        if (loggedInUser == null || loggedInUser.getDoctor() == null) {
            return false;
        }

        return loggedInUser.getDoctor().getId().equals(doctorId);
    }

    public boolean isPatientOwner(Authentication authentication, UUID patientId) {
        User loggedInUser = (User) authentication.getPrincipal();
        return patientRepository.findById(patientId)
                .map(patient -> patient.getUser().getId().equals(loggedInUser.getId()))
                .orElse(false);
    }
}
