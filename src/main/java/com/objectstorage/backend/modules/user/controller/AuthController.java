package com.objectstorage.backend.modules.user.controller;
import com.objectstorage.backend.common.ApiResponse;
import com.objectstorage.backend.common.ResponseBuilder;
import com.objectstorage.backend.common.response.ResponseMessages;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.objectstorage.backend.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register/")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseBuilder.success(
                HttpStatus.CREATED,
                ResponseMessages.created("User"),
                userService.register(dto)
        );
    }

}
