package com.realnaut.spaceships.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
