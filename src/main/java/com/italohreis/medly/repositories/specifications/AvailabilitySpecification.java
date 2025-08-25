package com.italohreis.medly.repositories.specifications;

import com.italohreis.medly.enums.AvailabilityStatus;
import com.italohreis.medly.enums.Speciality;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.UUID;

public class AvailabilitySpecification {

    public static Specification<Availability> isAvailable() {
        return (root, query, cb) -> cb.equal(root.get("status"), AvailabilityStatus.AVAILABLE);
    }

    public static Specification<Availability> isWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("startTime"), startDate),
                cb.lessThanOrEqualTo(root.get("endTime"), endDate)
        );
    }

    public static Specification<Availability> hasDoctorId(UUID doctorId) {
        return (root, query, cb) -> {
            Join<Availability, Doctor> doctorJoin = root.join("doctor");
            return cb.equal(doctorJoin.get("id"), doctorId);
        };
    }

    public static Specification<Availability> hasSpecialty(Speciality specialty) {
        return (root, query, cb) -> {
            Join<Availability, Doctor> doctorJoin = root.join("doctor");
            return cb.equal(doctorJoin.get("specialty"), specialty);
        };
    }
}