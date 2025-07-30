package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.models.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toModel(DoctorRequestDTO dto);

    @Mapping(target = "email", source = "user.email")
    DoctorResponseDTO toDto(Doctor doctor);
}
