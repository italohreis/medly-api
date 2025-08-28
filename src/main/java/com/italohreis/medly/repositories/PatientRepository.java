package com.italohreis.medly.repositories;

import com.italohreis.medly.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
}
