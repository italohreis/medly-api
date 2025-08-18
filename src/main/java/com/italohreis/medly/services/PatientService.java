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
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        userService.checkIfEmailExists(patientRequestDTO.email());

        User user = userService.createUser(
                patientRequestDTO.name(),
                patientRequestDTO.email(),
                patientRequestDTO.password(),
                Role.PATIENT
        );

        Patient patient = patientMapper.toModel(patientRequestDTO);

        patient.setUser(user);
        user.setPatient(patient);
        userRepository.save(user);

        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientResponseDTO updatePatient(UUID id, PatientUpdateDTO patientUpdateDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        User user = patient.getUser();

        if (StringUtils.hasText(patientUpdateDTO.name())) {
            user.setName(patientUpdateDTO.name());
        }

        if (StringUtils.hasText(patientUpdateDTO.email()) && !patientUpdateDTO.email().equalsIgnoreCase(user.getEmail())) {
            userService.checkIfEmailExists(patientUpdateDTO.email());
            user.setEmail(patientUpdateDTO.email());
        }

        if (StringUtils.hasText(patientUpdateDTO.cpf())) {
            patient.setCpf(patientUpdateDTO.cpf());
        }

        if (StringUtils.hasText(patientUpdateDTO.birthDate())) {
            patient.setBirthDate(LocalDate.parse(patientUpdateDTO.birthDate()));
        }

        userRepository.save(user);

        return patientMapper.toDto(patient);
    }
}
