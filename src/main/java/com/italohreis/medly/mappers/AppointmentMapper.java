package com.italohreis.medly.mappers;

import com.italohreis.medly.dtos.appointment.AppointmentResponseDTO;
import com.italohreis.medly.models.Appointment;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(source = "timeSlot.startTime", target = "startTime")
    @Mapping(source = "timeSlot.endTime", target = "endTime")
    @Mapping(source = "doctor", target = "doctor")
    @Mapping(source = "patient", target = "patient")
    AppointmentResponseDTO toDto(Appointment appointment);

    AppointmentResponseDTO.DoctorInfo toDoctorInfo(Doctor doctor);
    AppointmentResponseDTO.PatientInfo toPatientInfo(Patient patient);
}