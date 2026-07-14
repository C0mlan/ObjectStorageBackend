package com.objectstorage.backend.modules.user.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectstorage.backend.constants.routes.AuthRoutes;
import com.objectstorage.backend.modules.user.controller.AuthController;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import com.objectstorage.backend.modules.user.model.UserStatus;
import com.objectstorage.backend.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void register_shouldReturnCreated() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .email("john@example.com")
                .password("Password123!")
                .firstName("John")
                .lastName("Doe")
                .build();


        RegisterResponseDTO response = RegisterResponseDTO.builder()
                .id(UUID.randomUUID())
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .role("USER")
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.register(any(RegisterRequestDTO.class)))
                .thenReturn(response);

        mvc.perform(post(AuthRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(userService).register(any(RegisterRequestDTO.class));
    }
}