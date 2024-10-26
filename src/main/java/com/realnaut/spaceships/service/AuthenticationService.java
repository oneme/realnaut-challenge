package com.realnaut.spaceships.service;

import com.realnaut.spaceships.exception.ResourceNotFound;
import com.realnaut.spaceships.model.User;
import com.realnaut.spaceships.payload.LoginResponse;
import com.realnaut.spaceships.payload.LoginUserRequest;
import com.realnaut.spaceships.repository.UserRepository;
import com.realnaut.spaceships.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse authenticate(LoginUserRequest loginUserRequest) throws
            BadCredentialsException, ResourceNotFound {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword()
                )
        );
        User user = userRepository
                .findUserByEmail(loginUserRequest.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFound("User not found with this email :: " + loginUserRequest.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return loginResponse;
    }
}
