// src/main/java/com/italohreis/medly/security/SecurityService.java
package com.italohreis.medly.security;

import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.models.Appointment;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.AppointmentRepository;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;

    public boolean isDoctorOwner(Authentication authentication, UUID doctorId) {
        String userEmail = (String) authentication.getPrincipal();
        return userRepository.findByEmail(userEmail)
                .map(user -> user.getDoctor() != null && user.getDoctor().getId().equals(doctorId))
                .orElse(false);
    }

    public boolean isPatientOwner(Authentication authentication, UUID patientId) {
        String userEmail = (String) authentication.getPrincipal();
        return userRepository.findByEmail(userEmail)
                .map(user -> user.getPatient() != null && user.getPatient().getId().equals(patientId))
                .orElse(false);
    }

    public boolean isDoctorOwnerOfAvailability(Authentication authentication, UUID availabilityId) {
        String userEmail = (String) authentication.getPrincipal();

        User loggedInUser = userRepository.findByEmail(userEmail).orElse(null);
        if (loggedInUser == null || loggedInUser.getDoctor() == null) {
            return false;
        }

        return availabilityRepository.findById(availabilityId)
                .map(availability -> availability.getDoctor().getId().equals(loggedInUser.getDoctor().getId()))
                .orElse(false);
    }

    public boolean isUserPartOfAppointment(Authentication authentication, UUID appointmentId) {
        String userEmail = (String) authentication.getPrincipal();

        User loggedInUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    boolean isDoctor = loggedInUser.getDoctor() != null &&
                            appointment.getDoctor().getId().equals(loggedInUser.getDoctor().getId());
                    boolean isPatient = loggedInUser.getPatient() != null &&
                            appointment.getPatient().getId().equals(loggedInUser.getPatient().getId());
                    return isDoctor || isPatient;
                })
                .orElse(false);
    }

    public boolean isDoctorOwnerOfAppointment(Authentication authentication, UUID appointmentId) {
        String userEmail = (String) authentication.getPrincipal();

        User loggedInUser = userRepository.findByEmail(userEmail).orElse(null);
        if (loggedInUser == null || loggedInUser.getDoctor() == null) {
            return false;
        }

        return appointmentRepository.findById(appointmentId)
                .map(appointment -> appointment.getDoctor().getId().equals(loggedInUser.getDoctor().getId()))
                .orElse(false);
    }
}