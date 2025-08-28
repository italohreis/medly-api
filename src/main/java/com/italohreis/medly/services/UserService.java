package com.italohreis.medly.services;

import com.italohreis.medly.dtos.user.UserPasswordUpdateDTO;
import com.italohreis.medly.dtos.user.UserProfileResponseDTO;
import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.BusinessRuleException;
import com.italohreis.medly.exceptions.EmailAlreadyExistsException;
import com.italohreis.medly.mappers.UserMapper;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponseDTO getUserProfile(Authentication authentication) {
        String userEmail = (String) authentication.getPrincipal();

        User currentUser = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userMapper.toProfileDto(currentUser);
    }

    public void checkIfEmailExists(String email) {
        if (userRepository.findByEmailAndActiveTrue(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public User createUser(String name, String email, String password, Role role) {
        checkIfEmailExists(email);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }

    @Transactional
    public void changeUserPassword(Authentication authentication, UserPasswordUpdateDTO dto) {
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new BusinessRuleException("New password and confirmation password do not match.");
        }

        String userEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.currentPassword(), currentUser.getPassword())) {
            throw new BusinessRuleException("Incorrect current password.");
        }

        currentUser.setPassword(passwordEncoder.encode(dto.newPassword()));
    }
}
