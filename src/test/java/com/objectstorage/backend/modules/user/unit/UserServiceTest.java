package com.objectstorage.backend.modules.user.unit;
import com.objectstorage.backend.modules.user.dto.LoginRequestDTO;
import com.objectstorage.backend.modules.user.dto.LoginResponseDTO;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import org.springframework.security.core.Authentication;
import com.objectstorage.backend.modules.user.model.AuthProvider;
import com.objectstorage.backend.modules.user.model.Role;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.model.UserStatus;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.modules.user.service.UserService;
import com.objectstorage.backend.security.config.CustomUserDetails;
import com.objectstorage.backend.security.jwt.JwtService;
import com.objectstorage.backend.security.jwt.TokenStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenStore tokenStore;

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
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(validRequestDTO.getPassword()))
                .thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        RegisterResponseDTO result = userService.register(validRequestDTO);

        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("USER", result.getRole());
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode(validRequestDTO.getPassword());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    void loginservice_shouldReturnTokens() {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("john@example.com")
                .password("Password123!")
                .build();

        CustomUserDetails principal = mock(CustomUserDetails.class);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(principal);

        when(jwtService.generateAccessToken(principal))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(principal))
                .thenReturn("refresh-token");

        when(principal.getId())
                .thenReturn(UUID.randomUUID());

        LoginResponseDTO response = userService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateAccessToken(principal);
        verify(jwtService).generateRefreshToken(principal);
        verify(tokenStore)
                .saveRefreshToken(anyString(), any(), anyLong());
    }
}