package dev.xamet.dreamgamesstudy.repository;

import dev.xamet.dreamgamesstudy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer>{
    /**
     * Find a team by a given name
     */
    Team findByName(String name);

}
