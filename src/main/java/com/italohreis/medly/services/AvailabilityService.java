package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        availability.setDoctor(doctor);
        Availability savedAvailability = availabilityRepository.save(availability);
        return availabilityMapper.toDto(savedAvailability);
    }

    public List<Availability> getAvailabilitiesByDoctorId(UUID doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }
}
