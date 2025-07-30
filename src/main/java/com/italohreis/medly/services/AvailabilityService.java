package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
