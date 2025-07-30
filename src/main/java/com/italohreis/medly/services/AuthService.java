package com.italohreis.medly.services;

import com.italohreis.medly.dtos.auth.AuthRequestDTO;
import com.italohreis.medly.dtos.auth.AuthResponseDTO;
import com.italohreis.medly.exceptions.InvalidCredentialsException;
import com.italohreis.medly.exceptions.ResourceNotFoundException;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import com.italohreis.medly.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByEmail(authRequestDTO.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authRequestDTO.email()));

        if (!passwordEncoder.matches(authRequestDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }

}
