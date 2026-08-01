package com.objectstorage.backend.modules.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String accessToken;

    private String refreshToken;
}
