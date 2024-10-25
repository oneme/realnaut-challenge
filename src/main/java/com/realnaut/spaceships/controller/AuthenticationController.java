package com.realnaut.spaceships.controller;

import com.realnaut.spaceships.exception.ResourceNotFound;
import com.realnaut.spaceships.payload.LoginResponse;
import com.realnaut.spaceships.payload.LoginUserRequest;
import com.realnaut.spaceships.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest)
            throws ResourceNotFound {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserRequest));
    }
}
