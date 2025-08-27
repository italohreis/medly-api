package com.italohreis.medly.services;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.enums.AppointmentStatus;
import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AppointmentMapper;
import com.italohreis.medly.models.*;
import com.italohreis.medly.repositories.*;
import com.italohreis.medly.repositories.specs.AppointmentSpec;
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
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
        TimeSlot timeSlot = timeSlotRepository.findById(dto.timeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "id", dto.timeSlotId()));

        if (timeSlot.getStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BusinessRuleException("This time slot is not available for booking.");
        }

        Patient patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", dto.patientId()));

        timeSlot.setStatus(AvailabilityStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

        Appointment appointment = new Appointment();
        appointment.setTimeSlot(timeSlot);
        appointment.setDoctor(timeSlot.getAvailabilityWindow().getDoctor());
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
            spec = AppointmentSpec.hasPatientId(currentUser.getPatient().getId());
        } else if (currentUser.getRole() == Role.DOCTOR) {
            spec = AppointmentSpec.hasDoctorId(currentUser.getDoctor().getId());
            if (patientId != null) {
                spec = spec.and(AppointmentSpec.hasPatientId(patientId));
            }
        } else if (currentUser.getRole() == Role.ADMIN) {
            if (doctorId != null) {
                spec = AppointmentSpec.hasDoctorId(doctorId);
            }
            if (patientId != null) {
                Specification<Appointment> patientSpec = AppointmentSpec.hasPatientId(patientId);
                spec = (spec == null) ? patientSpec : spec.and(patientSpec);
            }
        }

        if (startDate != null && endDate != null) {
            Specification<Appointment> dateSpec = AppointmentSpec.isBetweenDates(startDate, endDate);
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
        if (currentStatus == AppointmentStatus.CANCELLED ||
            currentStatus == AppointmentStatus.COMPLETED) {
            throw new BusinessRuleException(
                    "Appointment cannot be canceled because its status is " + currentStatus.name().toLowerCase()
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlot.setStatus(AvailabilityStatus.AVAILABLE);
        timeSlotRepository.save(timeSlot);

        return appointmentMapper.toDto(appointmentRepository.save(appointment));
    }

    @Transactional
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

        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlot.setStatus(AvailabilityStatus.COMPLETED);

        return appointmentMapper.toDto(appointment);
    }
}
