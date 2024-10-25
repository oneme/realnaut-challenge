package com.realnaut.spaceships.repository;

import com.realnaut.spaceships.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    List<Spaceship> findSpaceshipByName(String name);
}
