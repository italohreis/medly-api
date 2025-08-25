package com.italohreis.medly.services;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.enums.AppointmentStatus;
import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AppointmentMapper;
import com.italohreis.medly.models.Appointment;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.AppointmentRepository;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.PatientRepository;
import com.italohreis.medly.repositories.UserRepository;
import com.italohreis.medly.repositories.specifications.AppointmentSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Availability availability = availabilityRepository.findById(appointmentRequestDTO.availabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Availability", "id", appointmentRequestDTO.availabilityId()));

        if (availability.getStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BusinessRuleException("Availability is not available for booking");
        }

        Patient patient = patientRepository.findById(appointmentRequestDTO.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", appointmentRequestDTO.patientId()));

        availability.setStatus(AvailabilityStatus.BOOKED);
        availabilityRepository.save(availability);

        Appointment appointment = new Appointment();
        appointment.setAvailability(availability);
        appointment.setDoctor(availability.getDoctor());
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    public Page<AppointmentResponseDTO> listAppointments(UUID doctorId, UUID patientId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authentication.getName()));

        Specification<Appointment> spec = null;

        if (currentUser.getRole() == Role.PATIENT) {
            spec = AppointmentSpecification.hasPatientId(currentUser.getPatient().getId());
        } else if (currentUser.getRole() == Role.DOCTOR) {
            spec = AppointmentSpecification.hasDoctorId(currentUser.getDoctor().getId());
            if (patientId != null) {
                spec = spec.and(AppointmentSpecification.hasPatientId(patientId));
            }
        } else if (currentUser.getRole() == Role.ADMIN) {
            if (doctorId != null) {
                spec = AppointmentSpecification.hasDoctorId(doctorId);
            }
            if (patientId != null) {
                Specification<Appointment> doctorSpec = AppointmentSpecification.hasDoctorId(doctorId);
                spec = (spec == null) ? doctorSpec : spec.and(doctorSpec);
            }
        }

        if (startDate != null && endDate != null) {
            Specification<Appointment> dateSpec = AppointmentSpecification.isBetweenDates(startDate, endDate);
            spec = (spec == null) ? dateSpec : spec.and(dateSpec);
        }

        return appointmentRepository.findAll(spec, pageable)
                .map(appointmentMapper::toDto);
    }

    public AppointmentResponseDTO getAppointmentById(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        AppointmentStatus currentStatus = appointment.getStatus();
        if (currentStatus == AppointmentStatus.COMPLETED ||
            currentStatus == AppointmentStatus.CANCELLED) {
            throw new BusinessRuleException("Appointment cannot be canceled because it's already " +
                    appointment.getStatus().name().toLowerCase()
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Availability availability = appointment.getAvailability();
        availability.setStatus(AvailabilityStatus.AVAILABLE);
        availabilityRepository.save(availability);

        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    public AppointmentResponseDTO completeAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        AppointmentStatus currentStatus = appointment.getStatus();
        if (currentStatus == AppointmentStatus.CANCELLED ||
            currentStatus == AppointmentStatus.COMPLETED) {
            throw new BusinessRuleException(
                    "Appointment cannot be completed because its status is " + currentStatus.name().toLowerCase()
            );
        }
        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }
}
