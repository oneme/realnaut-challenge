package com.realnaut.spaceships;

import com.realnaut.spaceships.core.exception.ResourceNotFound;
import com.realnaut.spaceships.core.model.User;
import com.realnaut.spaceships.application.payload.LoginResponse;
import com.realnaut.spaceships.application.payload.LoginUserRequest;
import com.realnaut.spaceships.infrastructure.repository.UserRepository;
import com.realnaut.spaceships.infrastructure.security.JwtService;
import com.realnaut.spaceships.core.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_shouldReturnLoginResponse() throws ResourceNotFound {
        User user = new User();
        user.setName("tester");
        user.setEmail("tester@realnaut.com");
        user.setEmail("$2a$10$8YiCghWvC9UK6/mpy/9lPuq3TZoDbjbMJY.1HnBdtDrRgw48/8i62");

        when(userRepository.findUserByEmail("tester@realnaut.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("tester@realnaut.com");
        loginUserRequest.setPassword("password");

        LoginResponse response = authenticationService.authenticate(loginUserRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(3600, response.getExpiresIn());
    }
}
