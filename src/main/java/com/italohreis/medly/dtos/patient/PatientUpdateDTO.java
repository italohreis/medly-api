package com.italohreis.medly.dtos.patient;

public record PatientUpdateDTO (
    String name,
    String email,
    String cpf,
    String birthDate
) {}
