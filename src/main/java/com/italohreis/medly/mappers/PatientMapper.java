package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.models.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toModel(PatientRequestDTO dto);

    @Mapping(target = "email", source = "user.email")
    PatientResponseDTO toDto(Patient patient);
}
