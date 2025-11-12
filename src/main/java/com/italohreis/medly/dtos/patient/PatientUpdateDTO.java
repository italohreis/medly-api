package com.italohreis.medly.dtos.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Schema(description = "Data transfer object for updating an existing patient's details. All fields are optional.")
public record PatientUpdateDTO (
        @Schema(description = "Patient's new full name.", example = "John A. Smith")
        String name,

        @Schema(description = "Patient's new email address.", example = "john.a.smith@example.com")
        @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @Schema(description = "Patient's new CPF number.", example = "987.654.321-00")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
        String cpf,

        @Schema(description = "Patient's new birth date (must be in the past).", example = "1990-02-20")
        @Past
        LocalDate birthDate
) {}