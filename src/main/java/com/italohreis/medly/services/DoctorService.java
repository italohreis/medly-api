package com.italohreis.medly.services;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.mappers.DoctorMapper;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        userService.checkIfEmailExists(doctorRequestDTO.email());

        User user = userService.createUser(
                doctorRequestDTO.name(),
                doctorRequestDTO.email(),
                doctorRequestDTO.password(),
                Role.DOCTOR
        );

        Doctor doctor = doctorMapper.toModel(doctorRequestDTO);

        user.setDoctor(doctor);
        doctor.setUser(user);
        userRepository.save(user);

        return doctorMapper.toDto(doctor);
    }
}
