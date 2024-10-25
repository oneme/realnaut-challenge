package com.realnaut.spaceships.payload;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceshipRequest {
    @NotNull(message = "Name is mandatory")
    private String name;
    private String series;
    private String type;

}
