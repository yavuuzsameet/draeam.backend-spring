package dev.xamet.dreamgamesstudy.controller;

import dev.xamet.dreamgamesstudy.utility.TokenGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @GetMapping("/token")
    public void refreshToken(@RequestParam int userId, HttpServletResponse response) {
        response.addHeader(TokenGenerator.ACCESS_TOKEN, TokenGenerator.generateToken(TokenGenerator.ACCESS_TOKEN, userId));
        response.addHeader(TokenGenerator.REFRESH_TOKEN, TokenGenerator.generateToken(TokenGenerator.REFRESH_TOKEN, userId));
    }
}
