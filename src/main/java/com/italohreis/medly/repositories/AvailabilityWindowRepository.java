package com.italohreis.medly.repositories;

import com.italohreis.medly.models.AvailabilityWindow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AvailabilityWindowRepository extends JpaRepository<AvailabilityWindow, UUID> {
    Page<AvailabilityWindow> findByDoctorId(UUID doctorId, Pageable pageable);

    @Query("SELECT aw FROM AvailabilityWindow aw WHERE aw.doctor.id = :doctorId AND aw.startTime < :newEndTime AND aw.endTime > :newStartTime")
    List<AvailabilityWindow> findOverlappingWindows(
            @Param("doctorId") UUID doctorId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime
    );
}
