package com.italohreis.medly.repositories;

import com.italohreis.medly.models.Appointment;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.id = :id AND a.deleted = false")
    @NonNull
    Optional<Appointment> findById(@Param("id") UUID id);

    @Override
    @Query("SELECT (COUNT(a) > 0) FROM Appointment a WHERE a.id = :id AND a.deleted = false")
    boolean existsById(@NonNull@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Appointment a SET a.deleted = true WHERE a.id = :id")
    void softDeleteById(@Param("id") UUID id);
}
