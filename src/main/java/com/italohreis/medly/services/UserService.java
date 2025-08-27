package com.italohreis.medly.services;

import com.italohreis.medly.enums.Role;
import com.italohreis.medly.exceptions.EmailAlreadyExistsException;
import com.italohreis.medly.models.User;
import com.italohreis.medly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
