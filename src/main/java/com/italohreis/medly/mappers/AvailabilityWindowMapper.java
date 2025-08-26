package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowRequestDTO;
import com.italohreis.medly.dtos.availabilityWindow.AvailabilityWindowResponseDTO;
import com.italohreis.medly.models.AvailabilityWindow;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvailabilityWindowMapper {
    AvailabilityWindowResponseDTO toDto(AvailabilityWindow availabilityWindow);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "timeSlots", ignore = true)
    AvailabilityWindow toModel(AvailabilityWindowRequestDTO dto);


    AvailabilityWindowResponseDTO.TimeSlotDTO timeSlotToTimeSlotDTO(TimeSlot timeSlot);

    AvailabilityWindowResponseDTO.DoctorInfo doctorToDoctorInfo(Doctor doctor);
}
