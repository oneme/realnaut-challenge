package com.realnaut.spaceships;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realnaut.spaceships.model.Spaceship;
import com.realnaut.spaceships.payload.SpaceshipRequest;
import com.realnaut.spaceships.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SpaceshipsControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        spaceshipRepository.deleteAll();
    }

    @Test
    void shouldCreateSpaceship() throws Exception {
        SpaceshipRequest spaceshipRequest = new SpaceshipRequest();
        spaceshipRequest.setName("Millennium Falcon");

        ResultActions response = mockMvc.perform(post("/api/v1/spaceships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Spaceship created successfully!")));
    }

    @Test
    void shouldFetchAllSpaceshipsPaged() throws Exception {
        Spaceship spaceship1 = new Spaceship();
        spaceship1.setName("X-Wing");

        Spaceship spaceship2 = new Spaceship();
        spaceship2.setName("TIE Fighter");

        spaceshipRepository.save(spaceship1);
        spaceshipRepository.save(spaceship2);

        ResultActions response = mockMvc.perform(get("/api/v1/spaceships")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("X-Wing")))
                .andExpect(jsonPath("$.content[1].name", is("TIE Fighter")));
    }

    @Test
    void shouldFetchSpaceshipById() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");
        spaceship = spaceshipRepository.save(spaceship);

        ResultActions response = mockMvc.perform(get("/api/v1/spaceships/{id}", spaceship.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("X-Wing")));
    }

    @Test
    void shouldFetchSpaceshipsByName() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");
        spaceshipRepository.save(spaceship);

        ResultActions response = mockMvc.perform(get("/api/v1/spaceships/search")
                .param("name", "X-Wing")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("X-Wing")));
    }

    @Test
    void shouldUpdateSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");
        spaceship = spaceshipRepository.save(spaceship);

        SpaceshipRequest updatedRequest = new SpaceshipRequest();
        updatedRequest.setName("X-Wing Updated");

        ResultActions response = mockMvc.perform(put("/api/v1/spaceships/{id}", spaceship.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Spaceship updated successfully")));
    }

    @Test
    void shouldDeleteSpaceship() throws Exception {
        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");
        spaceship = spaceshipRepository.save(spaceship);

        ResultActions response = mockMvc.perform(delete("/api/v1/spaceships/{id}", spaceship.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());
    }

    @Test
    void shouldFailToCreateSpaceshipWithoutName() throws Exception {
        SpaceshipRequest spaceshipRequest = new SpaceshipRequest();

        ResultActions response = mockMvc.perform(post("/api/v1/spaceships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceshipRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Body Validation Error")))
                .andExpect(jsonPath("$.message", is("Validation failed for one or more fields")))
                .andExpect(jsonPath("$.errors.name", is("Name is mandatory")));
    }

    @Test
    void shouldFailToUpdateSpaceshipWithoutName() throws Exception {

        Spaceship spaceship = new Spaceship();
        spaceship.setName("X-Wing");
        spaceship = spaceshipRepository.save(spaceship);

        SpaceshipRequest updatedRequest = new SpaceshipRequest();


        ResultActions response = mockMvc.perform(put("/api/v1/spaceships/{id}", spaceship.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Body Validation Error")))
                .andExpect(jsonPath("$.message", is("Validation failed for one or more fields")))
                .andExpect(jsonPath("$.errors.name", is("Name is mandatory")));
    }

}
