package com.objectstorage.backend.modules.user.integration;


import com.objectstorage.backend.common.ApiResponse;
import com.objectstorage.backend.config.AbstractTest;
import com.objectstorage.backend.constants.routes.AuthRoutes;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserIT extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO(
                "test@example.com",
                "Password123!",
                "testuser",
                "testuser"
        );

        MvcResult result = mockMvc.perform(post(AuthRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        ApiResponse<RegisterResponseDTO> body = objectMapper.readValue(
                json,
                new TypeReference<ApiResponse<RegisterResponseDTO>>() {}
        );

        assertNotNull(body);

        RegisterResponseDTO user = body.getData();

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getFirstName());

        assertTrue(userRepository.existsByEmail("test@example.com"));
    }


}
