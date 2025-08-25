package com.italohreis.medly.repositories;

import com.italohreis.medly.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID>, JpaSpecificationExecutor<TimeSlot> {
}
