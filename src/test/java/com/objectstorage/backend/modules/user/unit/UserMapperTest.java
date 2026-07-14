package com.objectstorage.backend.modules.user.unit;

import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import com.objectstorage.backend.modules.user.mapper.UserMapper;
import com.objectstorage.backend.modules.user.model.AuthProvider;
import com.objectstorage.backend.modules.user.model.Role;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.model.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {

    private User testUser;
    private UUID testUserId;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testDateTime = LocalDateTime.now();

        testUser = User.builder()
                .id(testUserId)
                .email("john@example.com")
                .password("encodedPassword123")
                .firstName("John")
                .lastName("Doe")
               .status(UserStatus.ACTIVE)
                .role(Role.USER)
               .authProvider(AuthProvider.EMAIL)
                .createdAt(testDateTime)
                .build();
    }


    @Test
    void shouldMapUserToDTOSuccessfully() {
        RegisterResponseDTO result = UserMapper.toDTO(testUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getRole()).isEqualTo("USER");
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(testDateTime);
    }



}
