package com.italohreis.medly.security.service;

import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.models.Appointment;
import com.italohreis.medly.models.TimeSlot;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.AppointmentRepository;
import com.italohreis.medly.repositories.AvailabilityWindowRepository;
import com.italohreis.medly.repositories.TimeSlotRepository;
import com.italohreis.medly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final AvailabilityWindowRepository availabilityWindowRepository;
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;

    public boolean isDoctorOwner(Authentication authentication, UUID doctorId) {
        User currentUser = getCurrentUser(authentication);
        return currentUser.getDoctor() != null && currentUser.getDoctor().getId().equals(doctorId);
    }

    public boolean isPatientOwner(Authentication authentication, UUID patientIdToCheck) {
        User currentUser = getCurrentUser(authentication);
        return currentUser.getPatient() != null && currentUser.getPatient().getId().equals(patientIdToCheck);
    }

    public boolean isDoctorOwnerOfAvailabilityWindow(Authentication authentication, UUID windowId) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getDoctor() == null) {
            return false;
        }

        return availabilityWindowRepository.findById(windowId)
                .map(window -> window.getDoctor().getId().equals(currentUser.getDoctor().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("AvailabilityWindow", "id", windowId));
    }

    public boolean isUserPartOfAppointment(Authentication authentication, UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        User currentUser = getCurrentUser(authentication);

        boolean isPatient = currentUser.getPatient() != null &&
                currentUser.getPatient().getId().equals(appointment.getPatient().getId());

        boolean isDoctor = currentUser.getDoctor() != null &&
                currentUser.getDoctor().getId().equals(appointment.getDoctor().getId());

        return isPatient || isDoctor;
    }

    public boolean isDoctorOwnerOfAppointment(Authentication authentication, UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        User currentUser = getCurrentUser(authentication);
        if (currentUser.getDoctor() == null) {
            return false;
        }

        return appointment.getDoctor().getId().equals(currentUser.getDoctor().getId());
    }

    public boolean isDoctorOwnerOfTimeSlot(Authentication authentication, UUID timeSlotId) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getDoctor() == null) {
            return false;
        }

        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "id", timeSlotId));

        UUID ownerDoctorId = timeSlot.getAvailabilityWindow().getDoctor().getId();
        return ownerDoctorId.equals(currentUser.getDoctor().getId());
    }

    private User getCurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String userEmail;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            userEmail = (String) principal;
        } else {
            throw new UsernameNotFoundException("Unable to extract username from authentication principal");
        }

        return userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }
}