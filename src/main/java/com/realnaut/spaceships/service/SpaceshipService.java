package com.realnaut.spaceships.service;

import com.realnaut.spaceships.exception.ResourceNotFound;
import com.realnaut.spaceships.model.Spaceship;
import com.realnaut.spaceships.payload.PagedResponse;
import com.realnaut.spaceships.payload.SpaceshipRequest;
import com.realnaut.spaceships.repository.SpaceshipRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpaceshipService {

    private final SpaceshipRepository repository;

    public SpaceshipService(SpaceshipRepository repository) {
        this.repository = repository;
    }
    @Cacheable(value = "spaceshipsCache", key = "#page + '-' + #size")
    public PagedResponse<Spaceship> getAllSpaceshipPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Spaceship> spaceship = this.repository.findAll(pageable);
        List<Spaceship> tmp = new ArrayList<>();
        spaceship.forEach(tmp::add);
        return new PagedResponse<>(
                tmp,
                spaceship.getNumber(),
                spaceship.getSize(),
                spaceship.getTotalElements(),
                spaceship.getTotalPages(),
                spaceship.isLast()
        );
    }
    public Spaceship createSpaceship(SpaceshipRequest spaceshipRequest) {
        Spaceship spaceship = new Spaceship();
        spaceship.setName(spaceshipRequest.getName());
        mapSpaceshipRequestToSpaceship(spaceshipRequest, spaceship);

        return repository.save(spaceship);
    }

    @Cacheable(value = "spaceshipsCache", key = "#name")
    public ResponseEntity<List<Spaceship>> getSpaceshipsByName(String name) {
        return ResponseEntity.ok(this.repository.searchByNameContainingIgnoreCase(name));
    }
    @Cacheable(value = "spaceshipsCache", key = "#id")
    public ResponseEntity<Spaceship> getSpaceshipById(Long id) throws ResourceNotFound {
        Spaceship spaceship = this.repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFound("Spaceship not found with id :: " + id));
        return ResponseEntity.ok(spaceship);
    }

    public void updateSpaceship(Long id, SpaceshipRequest spaceshipRequest) throws ResourceNotFound {
        Spaceship spaceship = this.repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFound("Spaceship not found with this id :: " + id));
        mapSpaceshipRequestToSpaceship(spaceshipRequest, spaceship);

        this.repository.save(spaceship);
    }


    public void deleteSpaceship(Long id) throws ResourceNotFound {
        Spaceship spaceship = this.repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFound("Spaceship not found with this id :: " + id));

        this.repository.delete(spaceship);
    }

    private void mapSpaceshipRequestToSpaceship(SpaceshipRequest spaceshipRequest, Spaceship spaceship) {
        spaceship.setName(spaceshipRequest.getName());
        if (null != spaceshipRequest.getSeries()) {
            spaceship.setSeries(spaceshipRequest.getSeries());
        }
        if (null != spaceshipRequest.getType()) {
            spaceship.setType(spaceshipRequest.getType());
        }
    }

}
