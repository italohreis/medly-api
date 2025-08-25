package com.italohreis.medly.repositories;

import com.italohreis.medly.models.AvailabilityWindow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvailabilityWindowRepository extends JpaRepository<AvailabilityWindow, UUID> {
    Page<AvailabilityWindow> findByDoctorId(UUID doctorId, Pageable pageable);
}
