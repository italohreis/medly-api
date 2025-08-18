package com.italohreis.medly.services;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.dtos.doctor.DoctorUpdateDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.DoctorMapper;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;

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

    @Transactional
    public DoctorResponseDTO updateDoctor(UUID id, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        User user = doctor.getUser();

        if (StringUtils.hasText(doctorUpdateDTO.name())) {
            user.setName(doctorUpdateDTO.name());
            doctor.setName(doctorUpdateDTO.name());
        }

        if (StringUtils.hasText(doctorUpdateDTO.email()) && !doctorUpdateDTO.email().equalsIgnoreCase(user.getEmail())) {
            userService.checkIfEmailExists(doctorUpdateDTO.email());
            user.setEmail(doctorUpdateDTO.email());
        }

        if (doctorUpdateDTO.speciality() != null) {
            doctor.setSpecialty(doctorUpdateDTO.speciality());
        }

        userRepository.save(user);

        return doctorMapper.toDto(doctor);
    }
}
