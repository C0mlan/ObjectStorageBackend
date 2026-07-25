package com.objectstorage.backend.modules.user.service;


import com.objectstorage.backend.modules.user.dto.LoginRequestDTO;
import com.objectstorage.backend.modules.user.dto.LoginResponseDTO;
import com.objectstorage.backend.modules.user.dto.RegisterRequestDTO;
import com.objectstorage.backend.modules.user.mapper.UserMapper;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import com.objectstorage.backend.common.util.EmailUtil;
import com.objectstorage.backend.common.exception.user.EmailAlreadyExists;
import com.objectstorage.backend.security.config.CustomUserDetails;
import com.objectstorage.backend.security.jwt.JwtService;
import com.objectstorage.backend.security.jwt.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.dto.RegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticateManager;
    private final JwtService jwtService;
    private final TokenStore tokenStore;


    @Value("${security.jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;

    /**
     * Registers a new user by validating email uniqueness, securely storing
     * the user's credentials, and returning the registered user's details.
     */
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        String email = EmailUtil.normalize(dto.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExists("Email already registered");
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(email)
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(java.time.LocalDateTime.now())
                .build();
        User saved = userRepository.save(user);

        return UserMapper.toDTO(saved);
    }

    /**
     * Authenticate the user's credentials, generate JWT access and refresh tokens,
     *  persist the refresh token in Redis for session management, and return the tokens
     *  to the client upon successful login.
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticateManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);
        tokenStore.saveRefreshToken(refreshToken, principal.getId(), refreshTokenExpiry);
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}