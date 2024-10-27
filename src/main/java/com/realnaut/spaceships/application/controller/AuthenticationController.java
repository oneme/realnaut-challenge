package com.realnaut.spaceships.application.controller;

import com.realnaut.spaceships.infrastructure.broker.MessageProducer;
import com.realnaut.spaceships.core.exception.ResourceNotFound;
import com.realnaut.spaceships.application.payload.LoginResponse;
import com.realnaut.spaceships.application.payload.LoginUserRequest;
import com.realnaut.spaceships.core.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MessageProducer messageProducer;


    public AuthenticationController(AuthenticationService authenticationService, MessageProducer messageProducer) {
        this.authenticationService = authenticationService;
        this.messageProducer = messageProducer;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest)
            throws BadCredentialsException, ResourceNotFound {
        messageProducer.sendMessage("login-topic",
                String.format("User with email: %s is attempting to login", loginUserRequest.getEmail()));
        return ResponseEntity.ok(authenticationService.authenticate(loginUserRequest));
    }
}
