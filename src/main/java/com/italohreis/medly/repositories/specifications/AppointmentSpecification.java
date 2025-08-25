package com.italohreis.medly.repositories.specifications;

import com.italohreis.medly.models.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentSpecification {
    public static Specification<Appointment> hasPatientId(UUID patientId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Appointment> hasDoctorId(UUID doctorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Appointment> isBetweenDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(
                        root.get("availability").get("startTime"),
                        startDateTime,
                        endDateTime
                );
    }
}