package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Speciality;

public record DoctorUpdateDTO(
   String name,
   String email,
   Speciality speciality
) {}
