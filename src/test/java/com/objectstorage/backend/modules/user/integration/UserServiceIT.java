package com.objectstorage.backend.modules.user.integration;

import com.objectstorage.backend.common.exception.user.EmailAlreadyExists;
import com.objectstorage.backend.config.AbstractTest;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIT extends AbstractTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "example@test.com",
                "password123",
                "John",
                "Doe"
        );

        RegisterResponseDTO response = userService.register(dto);

        User saved = userRepository.findByEmail("example@test.com").orElseThrow();

        assertEquals("John", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertTrue(passwordEncoder.matches("password123", saved.getPassword()));
        assertEquals(saved.getId(), response.getId());
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowEmailAlreadyExists() throws Exception{
        User existingUser =  User.builder()
                .email("exists@example.com")
                .password("encodedPassword")
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.saveAndFlush(existingUser);

        RegisterRequestDTO request = new RegisterRequestDTO(
                "exists@example.com",
                "P@ssw0rd1",
                "Jane",
                "Smith"

        );

        EmailAlreadyExists exception = assertThrows(
                EmailAlreadyExists.class,
                () -> userService.register(request)
        );

        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void shouldEncodePasswordBeforeSaving() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "john@example.com",
                "password123",
                "John",
                "Doe"

        );

        userService.register(dto);

        User savedUser = userRepository.findByEmail("john@example.com")
                .orElseThrow();

        assertNotEquals("password123", savedUser.getPassword());

        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    void shouldNormalizeEmailBeforeSaving() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "  john.Doe@Example.COM  ",
                "password123",
                "John",
                "Doe"

        );

        userService.register(dto);

        User saved = userRepository.findByEmail("john.doe@example.com")
                .orElseThrow();

        assertEquals("john.doe@example.com", saved.getEmail());
    }




}