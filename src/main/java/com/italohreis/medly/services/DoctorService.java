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
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {
        userService.checkIfEmailExists(dto.email());

        User user = userService.createUser(
                dto.name(),
                dto.email(),
                dto.password(),
                Role.DOCTOR
        );

        Doctor doctor = doctorMapper.toModel(dto);

        user.setDoctor(doctor);
        doctor.setUser(user);
        userRepository.save(user);

        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public DoctorResponseDTO updateDoctor(UUID id, DoctorUpdateDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        User user = doctor.getUser();

        if (StringUtils.hasText(dto.name())) {
            user.setName(dto.name());
            doctor.setName(dto.name());
        }

        if (StringUtils.hasText(dto.email()) && !dto.email().equalsIgnoreCase(user.getEmail())) {
            userService.checkIfEmailExists(dto.email());
            user.setEmail(dto.email());
        }

        if (dto.speciality() != null) {
            doctor.setSpecialty(dto.speciality());
        }

        userRepository.save(user);

        return doctorMapper.toDto(doctor);
    }
}
