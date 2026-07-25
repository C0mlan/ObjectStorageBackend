package com.objectstorage.backend.security.config;

import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HelperTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String email) {
        return userRepository.save(
                User.builder()
                        .email(email)
                        .password(passwordEncoder.encode("Password123!"))
                        .firstName("John")
                        .lastName("Doe")
                        .build()
        );
    }
}

