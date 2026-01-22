package com.italohreis.medly.services;

import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowRequestDTO;
import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotResponseDTO;
import com.italohreis.medly.dtos.timeslot.TimeSlotStatusUpdateDTO;
import com.italohreis.medly.enums.AppointmentStatus;
import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Specialty;
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
import com.italohreis.medly.repositories.specs.TimeSlotSpec;
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
        Specialty specialtyEnum = null;
        if (specialty != null && !specialty.isBlank()) {
            try {
                specialtyEnum = Specialty.valueOf(specialty.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException("Invalid specialty provided: " + specialty);
            }
        }

        Specification<TimeSlot> spec = TimeSlotSpec.isAvailable()
                .and(TimeSlotSpec.isWithinDateRange(startDate, endDate));

        if (doctorId != null) {
            spec = spec.and(TimeSlotSpec.hasDoctorId(doctorId));
        }
        if (specialtyEnum != null) {
            spec = spec.and(TimeSlotSpec.hasSpecialty(specialtyEnum));
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

        AvailabilityStatus newStatus = dto.status();
        AvailabilityStatus currentStatus = timeSlot.getStatus();


        if (currentStatus == newStatus) {
            throw new BusinessRuleException("The time slot is already in the requested status.");
        }

        if (currentStatus == AvailabilityStatus.BOOKED) {
            throw new BusinessRuleException("Cannot directly change the status of a booked time slot. Please cancel the appointment first.");
        }

        if (newStatus == AvailabilityStatus.BOOKED) {
            throw new BusinessRuleException("This endpoint can only be used to set status to AVAILABLE or BLOCKED.");
        }


        timeSlot.setStatus(dto.status());

        return timeSlotMapper.toDto(timeSlotRepository.save(timeSlot));
    }

    @Transactional
    public void deleteAvailabilityWindow(UUID windowId) {
        AvailabilityWindow window = availabilityWindowRepository.findByIdWithDoctor(windowId)
                .orElseThrow(() -> new ResourceNotFoundException("Window", "id", windowId));

        boolean hasActiveAppointments = window.getTimeSlots().stream()
                .anyMatch(slot -> slot.getAppointment() != null &&
                        slot.getAppointment().getStatus() != AppointmentStatus.CANCELLED &&
                        slot.getAppointment().getStatus() != AppointmentStatus.COMPLETED);

        if (hasActiveAppointments) {
            throw new BusinessRuleException("Cannot delete an availability window that has active appointments.");
        }

        window.getTimeSlots().forEach(slot -> {
            if (slot.getAppointment() != null) {
                slot.getAppointment().setTimeSlot(null);
            }
        });

        availabilityWindowRepository.delete(window);
    }
}
