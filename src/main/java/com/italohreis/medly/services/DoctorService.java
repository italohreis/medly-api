package com.italohreis.medly.services;

import com.italohreis.medly.dtos.doctor.DoctorRequestDTO;
import com.italohreis.medly.dtos.doctor.DoctorResponseDTO;
import com.italohreis.medly.dtos.doctor.DoctorUpdateDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.enums.Specialty;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.mappers.DoctorMapper;
import com.italohreis.medly.models.Doctor;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.DoctorRepository;
import com.italohreis.medly.repositories.UserRepository;
import com.italohreis.medly.repositories.specs.DoctorSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

        if (doctorRepository.existsByCrm(dto.crm())) {
            throw new BusinessRuleException("CRM already in use: " + dto.crm());
        }

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

        if (dto.specialty() != null) {
            doctor.setSpecialty(dto.specialty());
        }

        userRepository.save(user);

        return doctorMapper.toDto(doctor);
    }

    public Page<DoctorResponseDTO> getDoctors(String name, String speciality, String crm, Pageable pageable) {

        Specialty specialityEnum = null;
        if (speciality != null && !speciality.isBlank()) {
            try {
                specialityEnum = Specialty.valueOf(speciality.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException("Invalid speciality provided: " + speciality);
            }
        }

        Specification<Doctor> spec = DoctorSpec.isUserActive();

        if (name != null && !name.isBlank()) {
            spec = DoctorSpec.hasName(name);
        }
        if (crm != null && !crm.isBlank()) {
            spec = spec.and(DoctorSpec.hasCrm(crm));
        }
        if (specialityEnum != null) {
            spec = spec.and(DoctorSpec.hasSpecialty(specialityEnum));
        }

        return doctorRepository.findAll(spec, pageable)
                .map(doctorMapper::toDto);
    }

    public DoctorResponseDTO getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public void deleteDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        userRepository.softDeleteById(doctor.getUser().getId());
    }
}
