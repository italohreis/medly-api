package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import com.italohreis.medly.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "patient.id", target = "patientProfile.patientId")
    @Mapping(source = "doctor.id", target = "doctorProfile.doctorId")
    UserProfileResponseDTO toProfileDto(User user);
}
