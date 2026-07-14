package com.objectstorage.backend.modules.user.mapper;

import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import com.objectstorage.backend.modules.user.model.User;

public final class  UserMapper {
    private UserMapper() {
    }

    public static RegisterResponseDTO toDTO(User user) {
        return RegisterResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
