package com.realnaut.spaceships.repository;

import com.realnaut.spaceships.model.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Long> {
    List<Spaceship> findSpaceshipByNameContaining(String name);
    @Query("SELECT s FROM Spaceship s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Spaceship> searchByNameContainingIgnoreCase(@Param("name") String name);
}
