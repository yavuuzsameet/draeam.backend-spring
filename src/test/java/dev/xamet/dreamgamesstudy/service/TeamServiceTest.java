package dev.xamet.dreamgamesstudy.service;

import dev.xamet.dreamgamesstudy.entity.Team;
import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.exception.BadRequestException;
import dev.xamet.dreamgamesstudy.exception.NotFoundException;
import dev.xamet.dreamgamesstudy.repository.TeamRepository;
import dev.xamet.dreamgamesstudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private TeamService teamService;

    @Test
    void canCreateATeam() {
        String teamName = "Wrexham AFC";
        List<Integer> users = List.of(7);
        Team team = Team.builder().name(teamName).users(users).build();

        int USER_ID = 7;
        User user = User.builder().id(USER_ID).level(3).coins(7000).build();

        when(teamRepository.findByName(teamName)).thenReturn(null);
        when(teamRepository.findAll()).thenReturn(List.of(Team.builder().name("MK Dons").users(List.of(1, 2, 3)).build()));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        CompletableFuture<Team> teamCompletableFuture = teamService.createTeam(USER_ID, teamName);

        assertEquals(team.getName(), teamCompletableFuture.join().getName());
        assertEquals(team.getUsers(), teamCompletableFuture.join().getUsers());
    }

    @Test
    void canThrowExceptionWhenTeamNameIsAlreadyTaken() {
        String teamName = "Wrexham AFC";

        int USER_ID = 7;
        User user = User.builder().id(USER_ID).level(3).coins(7000).build();

        when(teamRepository.findByName(teamName)).thenReturn(Team.builder().name(teamName).users(List.of()).build());

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teamService.createTeam(user.getId(), teamName);
        });

        assertEquals("Team with name: " + teamName + " already exists", exception.getMessage());
    }

    @Test
    void canThrowExceptionWhenUserDoesNotExist() {
        String teamName = "Wrexham AFC";

        int USER_ID = 7;

        when(teamRepository.findByName(teamName)).thenReturn(null);
        when(teamRepository.findAll()).thenReturn(List.of(Team.builder().name("MK Dons").users(List.of(1, 2, 3)).build()));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(NotFoundException.class, () -> {
            teamService.createTeam(USER_ID, teamName);
        });

        assertEquals("User not found with id: " + USER_ID, exception.getMessage());
    }

    @Test
    void canThrowExceptionWhenTeamDoesNotExist() {
        int TEAM_ID = 0;

        int USER_ID = 7;
        User user = User.builder().id(USER_ID).level(3).coins(7000).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Throwable exception = assertThrows(NotFoundException.class, () -> {
            teamService.joinTeam(user.getId(), TEAM_ID);
        });

        assertEquals("Team not found with id: " + TEAM_ID, exception.getMessage());
    }

    @Test
    void canThrowExceptionWhenUserDoesNotHaveEnoughCoins() {

        String teamName = "Wrexham AFC";

        int USER_ID = 7;
        User user = User.builder().id(USER_ID).level(3).coins(500).build();

        when(teamRepository.findByName(teamName)).thenReturn(null);
        when(teamRepository.findAll()).thenReturn(List.of(Team.builder().name("MK Dons").users(List.of(1, 2, 3)).build()));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teamService.createTeam(user.getId(), teamName);
        });

        assertEquals("Not enough coins", exception.getMessage());
    }

    @Test
    void canJoinATeam() {
        String teamName = "Wrexham AFC";
        List<Integer> users = new ArrayList<>(); users.add(1);
        Team team = Team.builder().name(teamName).users(users).build();

        int USER_ID = 7;
        User user = User.builder().id(USER_ID).level(3).coins(7000).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        CompletableFuture<Team> teamCompletableFuture = teamService.joinTeam(USER_ID, team.getId());

        //expected
        team.getUsers().add(USER_ID);

        assertEquals(team.getUsers(), teamCompletableFuture.join().getUsers());
    }

    @Test
    void canThrowExceptionWhenTeamIsFull(){
        String teamName = "Wrexham AFC";
        List<Integer> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) { users.add(i); }

        Team team = Team.builder().name(teamName).users(users).build();

        int USER_ID = 20;
        User user = User.builder().id(USER_ID).level(3).coins(0).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teamService.joinTeam(USER_ID, team.getId());
        });

        assertEquals("Team is full", exception.getMessage());
    }

    @Test
    void canThrowExceptionWhenUserIsAlreadyInTheTeam(){
        String teamName = "Wrexham AFC";
        List<Integer> users = new ArrayList<>(); users.add(1);
        Team team = Team.builder().name(teamName).users(users).build();

        int USER_ID = 1;
        User user = User.builder().id(USER_ID).level(3).coins(0).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teamService.joinTeam(USER_ID, team.getId());
        });

        assertEquals("User is already in the team", exception.getMessage());
    }

    @Test
    void canThrowExceptionWhenUserIsAlreadyInAnotherTeam(){
        String teamName = "Wrexham AFC";
        List<Integer> users = new ArrayList<>(); users.add(1);
        Team team = Team.builder().name(teamName).users(users).build();

        String otherTeamName = "MK Dons";
        List<Integer> otherUsers = new ArrayList<>();
        Team otherTeam = Team.builder().name(otherTeamName).users(otherUsers).build();

        int USER_ID = 1;
        User user = User.builder().id(USER_ID).level(3).coins(0).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(teamRepository.findById(otherTeam.getId())).thenReturn(Optional.of(otherTeam));
        when(teamRepository.findAll()).thenReturn(List.of(team, otherTeam));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teamService.joinTeam(USER_ID, otherTeam.getId());
        });

        assertEquals("User is already in another team", exception.getMessage());
    }

    @Test
    void canGetTeams() {
        Team validTeam = Team.builder()
                .id(1)
                .name("Wrexham AFC")
                .users(List.of(1,2,3,4,5))
                .build();

        Team invalidTeam = Team.builder()
                .id(2)
                .name("MK Dons")
                .users(List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20))
                .build();

        when(teamRepository.findAll()).thenReturn(List.of(validTeam, invalidTeam));

        CompletableFuture<List<Integer>> teams = teamService.getTeams();

        assertEquals(1, teams.join().get(0));
        assertEquals(1, teams.join().size());
    }
}