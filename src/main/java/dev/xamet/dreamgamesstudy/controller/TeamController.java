package dev.xamet.dreamgamesstudy.controller;

import dev.xamet.dreamgamesstudy.entity.Team;
import dev.xamet.dreamgamesstudy.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/team")
    public CompletableFuture<Team> createTeam(@RequestParam int userId, @RequestParam String name) {
        return teamService.createTeam(userId, name);
    }

    @PutMapping("/team")
    public CompletableFuture<Team> joinTeam(@RequestParam int userId, @RequestParam int teamId) {
        return teamService.joinTeam(userId, teamId);
    }

    @GetMapping("/team")
    public CompletableFuture<List<Integer>> getTeams() {
        return teamService.getTeams();
    }

}
