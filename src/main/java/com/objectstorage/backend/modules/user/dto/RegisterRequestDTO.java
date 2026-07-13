package com.objectstorage.backend.modules.user.dto;

import com.objectstorage.backend.common.validation.ValidEmail;
import com.objectstorage.backend.common.validation.ValidPassword;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


}
