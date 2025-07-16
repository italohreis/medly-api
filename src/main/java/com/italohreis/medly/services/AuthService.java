package com.italohreis.medly.services;

import com.italohreis.medly.dtos.auth.AuthRequestDTO;
import com.italohreis.medly.dtos.auth.DoctorRegisterDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.dtos.auth.PatientRegisterDTO;
import com.italohreis.medly.exceptions.EmailAlreadyExistsException;
import com.italohreis.medly.exceptions.InvalidCredentialsException;
import com.italohreis.medly.exceptions.UserNotFoundException;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import com.italohreis.medly.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponseDTO registerPatient(PatientRegisterDTO patientRegisterDTO) {
        checkIfEmailExists(patientRegisterDTO.email());

        User user = new User();
        user.setName(patientRegisterDTO.name());
        user.setEmail(patientRegisterDTO.email());
        user.setPassword(passwordEncoder.encode(patientRegisterDTO.password()));
        user.setRole(Role.PATIENT);

        Patient patient = new Patient();
        patient.setName(patientRegisterDTO.name());
        patient.setCpf(patientRegisterDTO.cpf());
        patient.setBirthDate(patientRegisterDTO.birthDate());

        user.setPatient(patient);
        patient.setUser(user);

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponseDTO(token, user.getRole().name());
    }

    @Transactional
    public AuthResponseDTO registerDoctor(DoctorRegisterDTO doctorRegisterDTO) {
        checkIfEmailExists(doctorRegisterDTO.email());

        User user = new User();
        user.setName(doctorRegisterDTO.name());
        user.setEmail(doctorRegisterDTO.email());
        user.setPassword(passwordEncoder.encode(doctorRegisterDTO.password()));
        user.setRole(Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setName(doctorRegisterDTO.name());
        doctor.setCrm(doctorRegisterDTO.crm());
        doctor.setSpecialty(doctorRegisterDTO.specialty());

        user.setDoctor(doctor);
        doctor.setUser(user);

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByEmail(authRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException(authRequestDTO.email()));

        if (!passwordEncoder.matches(authRequestDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }

    private void checkIfEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }
    }

}
