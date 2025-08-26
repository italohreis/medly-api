package com.italohreis.medly.dtos.doctor;

import com.italohreis.medly.enums.Specialty;
import jakarta.validation.constraints.Email;

public record DoctorUpdateDTO(
   String name,

   @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
   String email,

   Specialty specialty
) {}
