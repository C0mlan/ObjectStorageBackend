package com.objectstorage.backend.modules.user.service;

import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.mapper.UserMapper;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.common.util.EmailUtil;
import com.objectstorage.backend.common.exception.user.EmailAlreadyExists;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        String email = EmailUtil.normalize(dto.getEmail());


        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExists("Email already registered");
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(email)
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(java.time.LocalDateTime.now())
                .build();
        User saved = userRepository.save(user);

        return UserMapper.toDTO(saved);
    }

}
