package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.availability.AvailabilityRequestDTO;
import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {

    @Mapping(target="doctor", source="doctorId")
    Availability toModel(AvailabilityRequestDTO dto);

    @Mapping(target="doctorId", source="doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    AvailabilityResponseDTO toDto(Availability availability);

    default Doctor mapDoctorIdToDoctor(UUID doctorId) {
        if (doctorId == null) return null;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return doctor;
    }
}
