package com.italohreis.medly.services;

import com.italohreis.medly.dtos.auth.AuthRequestDTO;
import com.italohreis.medly.dtos.auth.DoctorRegisterDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.dtos.auth.PatientRegisterDTO;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.Patient;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.PatientRepository;
import com.italohreis.medly.repositories.UserRepository;
import com.italohreis.medly.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponseDTO registerPatient(PatientRegisterDTO patientRegisterDTO) {
        User user = new User();
        user.setName(patientRegisterDTO.name());
        user.setEmail(patientRegisterDTO.email());
        user.setPassword(passwordEncoder.encode(patientRegisterDTO.password()));
        user.setRole(Role.PATIENT);
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setName(patientRegisterDTO.name());
        patient.setCpf(patientRegisterDTO.cpf());
        patient.setBirthDate(patientRegisterDTO.birthDate());
        patient.setUser(user);
        patientRepository.save(patient);

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponseDTO(token, user.getRole().name());
    }

    @Transactional
    public AuthResponseDTO registerDoctor(DoctorRegisterDTO doctorRegisterDTO) {
        User user = new User();
        user.setName(doctorRegisterDTO.name());
        user.setEmail(doctorRegisterDTO.email());
        user.setPassword(passwordEncoder.encode(doctorRegisterDTO.password()));
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(doctorRegisterDTO.name());
        doctor.setCrm(doctorRegisterDTO.crm());
        doctor.setSpecialty(doctorRegisterDTO.specialty());
        doctor.setUser(user);
        doctorRepository.save(doctor);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByEmail(authRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequestDTO.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }
}
