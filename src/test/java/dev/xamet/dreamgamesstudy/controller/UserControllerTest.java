package dev.xamet.dreamgamesstudy.controller;

import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;

    @Test
    void canCreateAUserAPIController() throws Exception {
        given(userService.createUser()).willAnswer(invocation ->
                CompletableFuture.completedFuture(User.builder().level(1).coins(5000).build())
        );

        ResultActions response = mockMvc.perform(post("/user"));

        response.andExpect(status().isCreated())
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("access-token")))
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("refresh-token")))
                .andExpect(result -> assertEquals(User.class, result.getAsyncResult().getClass()));
    }

    @Test
    void updateLevel() throws Exception {
        given(userService.updateLevel(ArgumentMatchers.anyInt())).willAnswer(invocation ->
                CompletableFuture.completedFuture(User.builder().level(2).coins(5000).build())
        );

        ResultActions response = mockMvc.perform(put("/user/1"));

        response.andExpect(status().isOk())
                .andExpect(result -> assertEquals(User.class, result.getAsyncResult().getClass()));
    }
}