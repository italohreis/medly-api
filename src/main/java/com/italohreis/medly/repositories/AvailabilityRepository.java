package com.italohreis.medly.repositories;

import com.italohreis.medly.models.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID>, JpaSpecificationExecutor<Availability> {
    Page<Availability> findByDoctorId(UUID doctorId, Pageable pageable);

    @Query("SELECT a FROM Availability a WHERE a.doctor.id = :doctorId AND a.startTime < :endTime AND a.endTime > :startTime")
    List<Availability> findOverlappingAvailabilities(
            @Param("doctorId") UUID doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
