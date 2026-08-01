package com.objectstorage.backend.modules.user.integration;


import com.objectstorage.backend.common.response.ResponseMessages;
import com.objectstorage.backend.constants.routes.AuthRoutes;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.security.config.AbstractTest;
import com.objectstorage.backend.security.config.HelperTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(HelperTest.class)
public class UserIT extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HelperTest helper;

    @Test
    void register_shouldCreateUser() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .email("john1@example.com")
                .password("Password123!")
                .firstName("John")
                .lastName("Doe")
                .build();

        mockMvc.perform(post(AuthRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("User created successfully."));

        assertTrue(userRepository.existsByEmail("john1@example.com"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        helper.createUser("john@example.com");

        String body = """
    {
      "email":"john@example.com",
      "password":"Password123!"
    }
    """;

        mockMvc.perform(post(AuthRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value(ResponseMessages.Auth.loginSuccessful()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }
}
