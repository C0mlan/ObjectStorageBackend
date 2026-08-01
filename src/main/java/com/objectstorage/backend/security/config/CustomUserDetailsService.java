package com.objectstorage.backend.security.config;

import com.objectstorage.backend.common.exception.user.UserNotFound;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found"));

        return new CustomUserDetails(user);
    }
}