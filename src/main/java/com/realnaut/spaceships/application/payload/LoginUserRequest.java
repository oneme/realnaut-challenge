package com.realnaut.spaceships.application.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {

    private String email;
    private String password;
}
