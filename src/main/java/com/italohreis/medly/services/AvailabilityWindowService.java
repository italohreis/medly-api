package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowRequestDTO;
import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotStatusUpdateDTO;
import com.italohreis.medly.enums.Speciality;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.AvailabilityWindowMapper;
import com.italohreis.medly.mappers.TimeSlotMapper;
import com.italohreis.medly.models.AvailabilityWindow;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.TimeSlot;
import com.italohreis.medly.repositories.AvailabilityWindowRepository;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.TimeSlotRepository;
import com.italohreis.medly.repositories.specifications.TimeSlotSpecification;
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
public class AvailabilityWindowService {

    private final AvailabilityWindowRepository availabilityWindowRepository;
    private final AvailabilityWindowMapper availabilityWindowMapper;
    private final DoctorRepository doctorRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    @Transactional
    public AvailabilityWindowResponseDTO createAvailabilityWindow(AvailabilityWindowRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.doctorId()).orElseThrow(
                () -> new ResourceNotFoundException("Doctor", "id", dto.doctorId())
        );

        List<AvailabilityWindow> overlappingWindows = availabilityWindowRepository.findOverlappingWindows(
                doctor.getId(),
                dto.startTime(),
                dto.endTime()
        );

        if (!overlappingWindows.isEmpty()) {
            throw new BusinessRuleException("The proposed availability window overlaps with an existing one.");
        }

        AvailabilityWindow window = new AvailabilityWindow();
        window.setDoctor(doctor);
        window.setStartTime(dto.startTime());
        window.setEndTime(dto.endTime());

        List<TimeSlot> slots = new ArrayList<>();
        LocalDateTime slotStart = dto.startTime();
        while (slotStart.isBefore(dto.endTime())) {
            LocalDateTime slotEnd = slotStart.plusMinutes(dto.slotDurationInMinutes());
            if (slotEnd.isAfter(dto.endTime())) {
                break;
            }

            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setAvailabilityWindow(window);
            timeSlot.setStartTime(slotStart);
            timeSlot.setEndTime(slotEnd);
            slots.add(timeSlot);

            slotStart = slotEnd;
        }

        window.setTimeSlots(slots);
        availabilityWindowRepository.save(window);

        return availabilityWindowMapper.toDto(window);
    }

    public Page<TimeSlotResponseDTO> searchAvailableTimeSlots(
                                                               UUID doctorId, String specialty, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Speciality specialityEnum = null;
        if (specialty != null && !specialty.isBlank()) {
            try {
                specialityEnum = Speciality.valueOf(specialty.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException("Invalid specialty provided: " + specialty);
            }
        }

        Specification<TimeSlot> spec = TimeSlotSpecification.isAvailable()
                .and(TimeSlotSpecification.isWithinDateRange(startDate, endDate));

        if (doctorId != null) {
            spec = spec.and(TimeSlotSpecification.hasDoctorId(doctorId));
        }
        if (specialityEnum != null) {
            spec = spec.and(TimeSlotSpecification.hasSpecialty(specialityEnum));
        }

        return timeSlotRepository.findAll(spec, pageable)
                .map(timeSlotMapper::toDto);
    }

    public Page<AvailabilityWindowResponseDTO> getDoctorSchedule(UUID doctorId, Pageable pageable) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }

        return availabilityWindowRepository.findByDoctorId(doctorId, pageable)
                .map(availabilityWindowMapper::toDto);
    }

    @Transactional
    public TimeSlotResponseDTO updateTimeSlotStatus(UUID id, TimeSlotStatusUpdateDTO dto) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "id", id));

        timeSlot.setStatus(dto.status());
        TimeSlot updatedTimeSlot = timeSlotRepository.save(timeSlot);

        return timeSlotMapper.toDto(updatedTimeSlot);
    }
}
