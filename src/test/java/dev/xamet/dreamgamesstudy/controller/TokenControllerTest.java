package dev.xamet.dreamgamesstudy.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TokenController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void canRefreshTokenAPIController() throws Exception {
        ResultActions response = mockMvc.
                perform(get("/token?userId=1"));

        response.andExpect(status().isOk())
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("access-token")))
                .andExpect(result -> assertNotNull(result.getResponse().getHeader("refresh-token")));
    }
}