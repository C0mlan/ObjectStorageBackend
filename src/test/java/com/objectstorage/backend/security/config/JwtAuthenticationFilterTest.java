package com.objectstorage.backend.security.config;

import com.objectstorage.backend.security.jwt.JwtAuthenticationFilter;
import com.objectstorage.backend.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String VALID_JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.valid_signature";
    private static final String INVALID_JWT_TOKEN = "invalid.jwt.token";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String BEARER_PREFIX = "Bearer ";

    @BeforeEach
    void setUp() {
        // Ensure no authentication exists before each test
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Clean up security context after each test to prevent state leakage
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("should continue filter when Authorization header is missing")
    void shouldContinueFilterWhenAuthorizationHeaderIsMissing() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(handlerExceptionResolver);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Basic dXNlcjpwYXNz", "Bearer", "token"})
    @DisplayName("should continue filter when Authorization header is not Bearer format")
    void shouldSkipAuthenticationWhenAuthorizationHeaderIsMissingOrNotBearer(String authHeader) throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(handlerExceptionResolver);
    }

    @Test
    @DisplayName("should set authentication in SecurityContext when token is valid")
    void shouldSetAuthenticationWhenTokenIsValid() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(TEST_EMAIL);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_JWT_TOKEN, userDetails)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_JWT_TOKEN, userDetails);
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    @DisplayName("should not set authentication when username extraction returns null")
    void shouldNotSetAuthenticationWhenUsernameIsNull() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verifyNoInteractions(userDetailsService);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(handlerExceptionResolver);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    @DisplayName("should not set authentication when token is invalid")
    void shouldNotSetAuthenticationWhenTokenIsInvalid() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(TEST_EMAIL);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_JWT_TOKEN, userDetails)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_JWT_TOKEN, userDetails);
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    @DisplayName("should not set authentication when SecurityContext already has authentication")
    void shouldNotOverwriteExistingAuthentication() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(TEST_EMAIL);

        // Set existing authentication
        Authentication existingAuth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(handlerExceptionResolver);
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertSame(existingAuth, authentication);
    }

    @Test
    @DisplayName("should resolve exception when JwtService throws during username extraction")
    void shouldResolveExceptionWhenJwtServiceThrowsDuringExtraction() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + INVALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        RuntimeException jwtException = new RuntimeException("Invalid JWT format");
        when(jwtService.extractUsername(INVALID_JWT_TOKEN)).thenThrow(jwtException);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(INVALID_JWT_TOKEN);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), eq(jwtException));
        verifyNoInteractions(userDetailsService);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("should resolve exception when UserDetailsService throws exception")
    void shouldResolveExceptionWhenUserDetailsServiceThrows() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(TEST_EMAIL);
        RuntimeException userException = new RuntimeException("User not found");
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenThrow(userException);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), eq(userException));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("should resolve exception when JwtService throws during token validation")
    void shouldResolveExceptionWhenJwtServiceThrowsDuringValidation() throws Exception {
        // Arrange
        String authHeader = BEARER_PREFIX + VALID_JWT_TOKEN;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(VALID_JWT_TOKEN)).thenReturn(TEST_EMAIL);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        RuntimeException validationException = new RuntimeException("Token validation failed");
        when(jwtService.isTokenValid(VALID_JWT_TOKEN, userDetails)).thenThrow(validationException);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(jwtService).extractUsername(VALID_JWT_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_JWT_TOKEN, userDetails);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), eq(validationException));
        verify(filterChain, never()).doFilter(request, response);
    }


}
