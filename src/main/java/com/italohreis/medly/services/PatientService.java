package com.italohreis.medly.services;

import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.dtos.patient.PatientUpdateDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.PatientMapper;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.PatientRepository;
import com.italohreis.medly.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        userService.checkIfEmailExists(dto.email());

        User user = userService.createUser(
                dto.name(),
                dto.email(),
                dto.password(),
                Role.PATIENT
        );

        Patient patient = patientMapper.toModel(dto);

        patient.setUser(user);
        user.setPatient(patient);
        userRepository.save(user);

        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientResponseDTO updatePatient(UUID id, PatientUpdateDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        User user = patient.getUser();

        if (StringUtils.hasText(dto.name())) {
            user.setName(dto.name());
            patient.setName(dto.name());
        }

        if (StringUtils.hasText(dto.email()) && !dto.email().equalsIgnoreCase(user.getEmail())) {
            userService.checkIfEmailExists(dto.email());
            user.setEmail(dto.email());
        }

        if (StringUtils.hasText(dto.cpf())) {
            patient.setCpf(dto.cpf());
        }

        if (dto.birthDate() != null) {
            patient.setBirthDate(dto.birthDate());
        }

        userRepository.save(user);

        return patientMapper.toDto(patient);
    }
}
