package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.dtos.availability.AvailabilityUpdateStatusDTO;
import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Speciality;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.specifications.AvailabilitySpecification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilityMapper availabilityMapper;

    @Transactional
    public AvailabilityResponseDTO createAvailability(Availability availability) {
        Doctor doctor = doctorRepository.findById(availability.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", availability.getDoctor().getId()));

        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new BusinessRuleException("Start time must be before end time.");
        }

        List<Availability> overlappingAvailabilities = availabilityRepository.findOverlappingAvailabilities(
                doctor.getId(), availability.getStartTime(), availability.getEndTime());

        if (!overlappingAvailabilities.isEmpty()) {
            throw new BusinessRuleException("Availability overlaps with existing availabilities.");
        }

        availability.setDoctor(doctor);
        Availability savedAvailability = availabilityRepository.save(availability);
        return availabilityMapper.toDto(savedAvailability);
    }

    public Page<AvailabilityResponseDTO> getAvailabilitiesByDoctorId(UUID doctorId, Pageable pageable) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }

        Page<Availability> availabilityPage = availabilityRepository.findByDoctorId(doctorId, pageable);
        if (availabilityPage.isEmpty()) {
            throw new ResourceNotFoundException("Availabilities for doctor", "id", doctorId);
        }

        return availabilityPage.map(availabilityMapper::toDto);

    }

    public Page<AvailabilityResponseDTO> searchAvailabilities(
            UUID doctorId, String specialty, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {

        Speciality specialityEnum = null;
        if (specialty != null && !specialty.isBlank()) {
            try {
                specialityEnum = Speciality.valueOf(specialty.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException("Invalid specialty provided: " + specialty);
            }
        }

        Specification<Availability> spec = AvailabilitySpecification.isAvailable();

        spec = spec.and(AvailabilitySpecification.isWithinDateRange(startDate, endDate));

        if (doctorId != null) {
            spec = spec.and(AvailabilitySpecification.hasDoctorId(doctorId));
        }
        if (specialityEnum != null) {
            spec = spec.and(AvailabilitySpecification.hasSpecialty(specialityEnum));
        }

        return availabilityRepository.findAll(spec, pageable)
                .map(availabilityMapper::toDto);
    }

    @Transactional
    public AvailabilityResponseDTO updateAvailabilityStatus(UUID availabilityId, AvailabilityUpdateStatusDTO availabilityUpdateStatusDTO) {
        AvailabilityStatus newStatus = availabilityUpdateStatusDTO.status();
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability", "id", availabilityId));

        AvailabilityStatus currentStatus = availability.getStatus();

        if (currentStatus == newStatus) {
            throw new BusinessRuleException("The availability is already in the requested status.");
        }

        switch (newStatus) {
            case AVAILABLE:
                if (currentStatus == AvailabilityStatus.BOOKED) {
                    throw new BusinessRuleException("This time has an appointment. Cancel an appointment to free it up.");
                }
                availability.setStatus(AvailabilityStatus.AVAILABLE);
                break;
            case BLOCKED:
                if (currentStatus == AvailabilityStatus.BOOKED) {
                    throw new BusinessRuleException("This time has an appointment. Cancel an appointment to block it.");
                }
                availability.setStatus(AvailabilityStatus.BLOCKED);
                break;

            case BOOKED:
                throw new BusinessRuleException("Cannot change status to BOOKED directly. Please use the appointment service to book an appointment.");
        }

        return availabilityMapper.toDto(availabilityRepository.save(availability));
    }
}
