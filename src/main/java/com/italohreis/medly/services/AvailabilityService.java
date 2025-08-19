package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.enums.Speciality;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.repositories.AvailabilityRepository;
import com.italohreis.medly.repositories.DoctorRepository;
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

        Specification<Availability> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Availability, Doctor> doctorJoin = root.join("doctor");

            predicates.add(criteriaBuilder.isTrue(root.get("isAvailable")));

            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startDate));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endDate));

            if (doctorId != null) {
                predicates.add(criteriaBuilder.equal(doctorJoin.get("id"), doctorId));
            }

            if (specialty != null && !specialty.isBlank()) {
                try {
                    Speciality specialityEnum = Speciality.valueOf(specialty.toUpperCase());
                    predicates.add(criteriaBuilder.equal(doctorJoin.get("specialty"), specialityEnum));
                } catch (IllegalArgumentException e) {
                    throw new BusinessRuleException("Invalid specialty provided: " + specialty);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Availability> results = availabilityRepository.findAll(spec, pageable);
        return results.map(availabilityMapper::toDto);
    }
}
