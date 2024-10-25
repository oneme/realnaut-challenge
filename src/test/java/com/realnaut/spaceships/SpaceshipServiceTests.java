package com.realnaut.spaceships;

import com.realnaut.spaceships.exception.ResourceNotFound;
import com.realnaut.spaceships.model.Spaceship;
import com.realnaut.spaceships.payload.PagedResponse;
import com.realnaut.spaceships.payload.SpaceshipRequest;
import com.realnaut.spaceships.repository.SpaceshipRepository;
import com.realnaut.spaceships.service.SpaceshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpaceshipServiceTests {

    @Mock
    private SpaceshipRepository spaceshipRepository;

    @InjectMocks
    private SpaceshipService spaceshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSpaceshipPaged_shouldReturnPagedResponse() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("Millennium Falcon");
        Page<Spaceship> spaceshipPage = new PageImpl<>(Collections.singletonList(spaceship));

        when(spaceshipRepository.findAll(any(PageRequest.class))).thenReturn(spaceshipPage);

        PagedResponse<Spaceship> response = spaceshipService.getAllSpaceshipPaged(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Millennium Falcon", response.getContent().get(0).getName());
    }

    @Test
    void createSpaceship_shouldReturnCreatedShip() {
        SpaceshipRequest request = new SpaceshipRequest();
        request.setName("X-Wing");

        Spaceship spaceship = new Spaceship();
        spaceship.setName(request.getName());

        when(spaceshipRepository.save(any(Spaceship.class))).thenReturn(spaceship);

        Spaceship result = spaceshipService.createSpaceship(request);

        assertNotNull(result);
        assertEquals("X-Wing", result.getName());
        verify(spaceshipRepository, times(1)).save(any(Spaceship.class));
    }

    @Test
    void getSpaceshipsByName_shouldReturnListOfSpaceships() {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");

        when(spaceshipRepository.searchByNameContainingIgnoreCase("Wing")).thenReturn(Collections.singletonList(spaceship));

        ResponseEntity<List<Spaceship>> response = spaceshipService.getSpaceshipsByName("Wing");

        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("X-Wing", response.getBody().get(0).getName());
    }

    @Test
    void getSpaceshipById_shouldReturnSpaceship_whenIdExists() throws ResourceNotFound {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("X-Wing");

        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceship));

        ResponseEntity<Spaceship> response = spaceshipService.getSpaceshipById(1L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("X-Wing", response.getBody().getName());
    }

    @Test
    void getSpaceshipById_shouldThrowException_whenIdDoesNotExist() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> spaceshipService.getSpaceshipById(1L));
    }

    @Test
    void updateSpaceship_shouldUpdateSpaceship_whenIdExists() throws ResourceNotFound {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("X-Wing");

        SpaceshipRequest request = new SpaceshipRequest();
        request.setName("X-Wing Updated");

        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceship));
        when(spaceshipRepository.save(spaceship)).thenReturn(spaceship);

        spaceshipService.updateSpaceship(1L, request);

        verify(spaceshipRepository, times(1)).findById(1L);
        verify(spaceshipRepository, times(1)).save(spaceship);
        assertEquals("X-Wing Updated", spaceship.getName());
    }

    @Test
    void deleteSpaceship_shouldDeleteSpaceship_whenIdExists() throws ResourceNotFound {
        Spaceship spaceship = new Spaceship();
        spaceship.setId(1L);

        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceship));
        doNothing().when(spaceshipRepository).delete(spaceship);

        spaceshipService.deleteSpaceship(1L);

        verify(spaceshipRepository, times(1)).findById(1L);
        verify(spaceshipRepository, times(1)).delete(spaceship);
    }

    @Test
    void deleteSpaceship_shouldThrowException_whenIdDoesNotExist() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> spaceshipService.deleteSpaceship(1L));
    }
}
