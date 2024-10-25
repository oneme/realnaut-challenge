package com.realnaut.spaceships.controller;

import com.realnaut.spaceships.exception.ResourceNotFound;
import com.realnaut.spaceships.model.Spaceship;
import com.realnaut.spaceships.payload.ApiResponse;
import com.realnaut.spaceships.payload.PagedResponse;
import com.realnaut.spaceships.payload.SpaceshipRequest;
import com.realnaut.spaceships.service.SpaceshipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/spaceships")
public class SpaceshipController {

    private final SpaceshipService service;

    public SpaceshipController(SpaceshipService service) {
        this.service = service;
    }

    @GetMapping
    public PagedResponse<Spaceship> getAllSpaceshipsPaged(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "5") int size) {

        return this.service.getAllSpaceshipPaged(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spaceship> getSpaceshipById(@PathVariable Long id) throws ResourceNotFound {
        return service.getSpaceshipById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Spaceship>> getSpaceshipsByName(@RequestParam String name) {
        return service.getSpaceshipsByName(name);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSpaceship(@Valid @RequestBody SpaceshipRequest spaceshipRequest)
            throws ResourceNotFound {
        Spaceship spaceship = service.createSpaceship(spaceshipRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(spaceship.getId()).toUri();
        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "Spaceship created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSpaceship(
            @PathVariable Long id,
            @Valid @RequestBody SpaceshipRequest spaceshipRequest)
            throws ResourceNotFound {

        service.updateSpaceship(id, spaceshipRequest);

        return ResponseEntity.ok(new ApiResponse(true, "Spaceship updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpaceship(@PathVariable Long id) throws ResourceNotFound {

        service.deleteSpaceship(id);

        return ResponseEntity.noContent().build();
    }
}
