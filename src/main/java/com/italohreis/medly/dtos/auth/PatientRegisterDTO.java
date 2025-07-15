package com.italohreis.medly.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record PatientRegisterDTO(
    @NotBlank
    String name,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String password,

    @NotBlank
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
    String cpf,

    @NotNull
    @Past
    LocalDate birthDate
){}