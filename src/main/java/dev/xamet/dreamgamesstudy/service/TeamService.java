package dev.xamet.dreamgamesstudy.service;

import dev.xamet.dreamgamesstudy.entity.Team;
import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.exception.*;
import dev.xamet.dreamgamesstudy.repository.TeamRepository;
import dev.xamet.dreamgamesstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class TeamService {

    /*
     * 20 is the maximum number of players in a team
     */
    private final int capacity = 20;

    /*
     Injections
     */
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    /*
     * Checks if a team with the given name already exists
     * Checks if the user is already in a team
     * Checks if user has enough coins to create a team
     * Creates a team with the given name and adds the user to it
     */
    @Async
    public CompletableFuture<Team> createTeam(int userId, String name) {

        checkTeamExistenceByName(name);
        checkUserHasTeam(userId);

        User user = getUserById(userId);
        long coins = user.getCoins();
        if (coins < 1000) {
            throw new BadRequestException("Not enough coins");
        }
        user.setCoins(coins - 1000);
        userRepository.save(user);

        Team team = new Team();
        team.setName(name);
        team.setUsers(Collections.singletonList(userId));
        return CompletableFuture.completedFuture(teamRepository.save(team));
    }

    /*
     * Checks if the user exists, returns the user if it does
     * Checks if the team exists, returns the team if it does
     * Checks if the team is full
     * Checks if the user is already in the team
     * Checks if the user is already in a team
     * Adds the user to the team
     */
    @Async
    public CompletableFuture<Team> joinTeam(int userId, int teamId) {

        User user = getUserById(userId);
        Team team = getTeamById(teamId);

        checkTeamLimits(team, userId);
        checkUserHasTeam(userId);

        team.getUsers().add(userId);
        return CompletableFuture.completedFuture(teamRepository.save(team));
    }

    /*
     * Returns a list of 10 random teams with a capacity less than the maximum capacity
     */
    @Async
    public CompletableFuture<List<Integer>> getTeams() {
        List<Integer> teams = new ArrayList<>(teamRepository.findAll().stream().filter(team -> team.getUsers().size() < capacity).map(Team::getId).toList());
        Collections.shuffle(teams);
        return CompletableFuture.completedFuture(teams.stream().limit(10).toList());
    }

    /*
     * Checks if the team is full
     * Checks if the user is already in the team
     */
    private void checkTeamLimits(Team team, int userId) {
        if (team.getUsers().size() >= capacity) {
            throw new BadRequestException("Team is full");
        }

        if (team.getUsers().contains(userId)) {
            throw new BadRequestException("User is already in the team");
        }
    }

    /*
     * Checks if the user is already in a team
     */
    private void checkUserHasTeam(int userId) {
        teamRepository.findAll().forEach(team -> {
            if (team.getUsers().contains(userId)) {
                throw new BadRequestException("User is already in another team");
            }
        });

    }

    /*
     * Checks if a team with the given name already exists
     */
    private void checkTeamExistenceByName(String name) {
        if(teamRepository.findByName(name) != null) {
            throw new BadRequestException("Team with name: " + name + " already exists");
        }
    }

    /*
     * Checks if the user exists, returns the user if it does
     */
    private User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId)
        );
    }

    /*
     * Checks if the team exists, returns the team if it does
     */
    private Team getTeamById(int teamId) {
        return teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found with id: " + teamId)
        );
    }


}
