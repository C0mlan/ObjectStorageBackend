package com.objectstorage.backend.modules.user.dto;

import com.objectstorage.backend.modules.user.model.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

import java.util.UUID;

@Data
@Builder
public class RegisterResponseDTO {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;

    private String role;

    private UserStatus status;

    private LocalDateTime createdAt;


}