package com.objectstorage.backend.modules.user.unit;
//import com.objectstorage.backend.common.exception.user.EmailAlreadyExists;
//import com.objectstorage.backend.common.util.EmailUtil;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
//import com.objectstorage.backend.modules.user.mapper.UserMapper;
import com.objectstorage.backend.modules.user.model.AuthProvider;
import com.objectstorage.backend.modules.user.model.Role;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.model.UserStatus;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.modules.user.service.UserService;
//import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequestDTO validRequestDTO;
    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        validRequestDTO = RegisterRequestDTO.builder()
                .email("test@example.com")
                .password("TestPassword123!")
                .firstName("John")
                .lastName("Doe")
                .build();

        testUser = User.builder()
                .id(testUserId)
                .email("test@example.com")
                .password("encodedPassword123")
                .firstName("John")
                .lastName("Doe")
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .authProvider(AuthProvider.EMAIL)
                .createdAt(LocalDateTime.now())
                .build();
    }


    @Test
    @DisplayName("should successfully register user with valid request")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(validRequestDTO.getPassword()))
                .thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        RegisterResponseDTO result = userService.register(validRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getRole()).isEqualTo("USER");
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);

        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode(validRequestDTO.getPassword());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }
}