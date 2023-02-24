package dev.xamet.dreamgamesstudy.controller;

import dev.xamet.dreamgamesstudy.entity.Team;
import dev.xamet.dreamgamesstudy.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TeamService teamService;

    @Test
    void canCreateATeamAPIController() throws Exception {
        given(teamService.createTeam(anyInt(), anyString())).willAnswer(invocation ->
                CompletableFuture.completedFuture(Team.builder().name("Wrexham AFC").build())
        );

        ResultActions response = mockMvc.perform(post("/team?userId=1&name=Wrexham AFC"));

        response.andExpect(status().isOk())
                .andExpect(result -> assertEquals("Wrexham AFC", ((Team) result.getAsyncResult()).getName()))
                .andExpect(result -> assertEquals(Team.class, result.getAsyncResult().getClass()));
    }

    @Test
    void canJoinATeamAPIController() throws Exception {
        given(teamService.joinTeam(anyInt(), anyInt())).willAnswer(invocation ->
                CompletableFuture.completedFuture(Team.builder().name("Wrexham AFC").build())
        );

        ResultActions response = mockMvc.perform(put("/team?userId=1&teamId=1"));

        response.andExpect(status().isOk())
                .andExpect(result -> assertEquals("Wrexham AFC", ((Team) result.getAsyncResult()).getName()))
                .andExpect(result -> assertEquals(Team.class, result.getAsyncResult().getClass()));
    }

    @Test
    void canGetTeamsAPIController() throws Exception {
        given(teamService.getTeams()).willAnswer(invocation ->
                CompletableFuture.completedFuture(List.of(1,2,3))
        );

        ResultActions response = mockMvc.perform(get("/team"));

        response.andExpect(status().isOk())
                .andExpect(result -> assertEquals(List.of(1,2,3), result.getAsyncResult()));
    }
}