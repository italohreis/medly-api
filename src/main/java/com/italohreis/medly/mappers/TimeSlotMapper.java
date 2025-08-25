package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.timeslot.TimeSlotResponseDTO;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {

    @Mapping(source = "availabilityWindow.doctor", target = "doctor")
    TimeSlotResponseDTO toDto(TimeSlot timeSlot);

    TimeSlotResponseDTO.DoctorInfo doctorToDoctorInfo(Doctor doctor);
}
