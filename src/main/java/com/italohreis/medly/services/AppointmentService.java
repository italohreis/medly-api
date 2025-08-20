package com.italohreis.medly.services;

import com.italohreis.medly.dtos.appointment.AppointmentRequestDTO;
import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.enums.AppointmentStatus;
import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AppointmentMapper;
import com.italohreis.medly.models.Appointment;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.repositories.AppointmentRepository;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

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
}
