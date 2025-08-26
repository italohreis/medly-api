package com.italohreis.medly.repositories.specs;

import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Specialty;
import com.italohreis.medly.models.AvailabilityWindow;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.TimeSlot;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class TimeSlotSpec {
    public static Specification<TimeSlot> isAvailable() {
        return (root, query, cb) -> cb.equal(root.get("status"), AvailabilityStatus.AVAILABLE);
    }

    public static Specification<TimeSlot> isWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> cb.between(root.get("startTime"), startDate, endDate);
    }

    public static Specification<TimeSlot> hasDoctorId(UUID doctorId) {
        return (root, query, cb) -> {
            Join<TimeSlot, AvailabilityWindow> windowJoin = root.join("availabilityWindow");
            Join<AvailabilityWindow, Doctor> doctorJoin = windowJoin.join("doctor");
            return cb.equal(doctorJoin.get("id"), doctorId);
        };
    }

    public static Specification<TimeSlot> hasSpecialty(Specialty specialty) {
        return (root, query, cb) -> {
            Join<TimeSlot, AvailabilityWindow> windowJoin = root.join("availabilityWindow");
            Join<AvailabilityWindow, Doctor> doctorJoin = windowJoin.join("doctor");
            return cb.equal(doctorJoin.get("specialty"), specialty);
        };
    }
}
