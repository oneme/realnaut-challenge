package com.realnaut.spaceships.application.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {

    private Boolean success;
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
