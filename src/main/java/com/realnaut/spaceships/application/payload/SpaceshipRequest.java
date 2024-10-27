package com.realnaut.spaceships.application.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceshipRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String series;
    private String type;

}
