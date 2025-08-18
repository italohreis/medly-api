package com.italohreis.medly.services;

import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.dtos.patient.PatientRequestDTO;
import com.italohreis.medly.dtos.patient.PatientResponseDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.mappers.PatientMapper;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
}
