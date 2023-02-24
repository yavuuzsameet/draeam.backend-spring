package dev.xamet.dreamgamesstudy.controller;

import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.service.UserService;
import dev.xamet.dreamgamesstudy.utility.TokenGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public CompletableFuture<User> createUser(HttpServletResponse response) throws ExecutionException, InterruptedException {
        CompletableFuture<User> user = userService.createUser();

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(TokenGenerator.ACCESS_TOKEN, TokenGenerator.generateToken(TokenGenerator.ACCESS_TOKEN, user.get().getId()));
        response.addHeader(TokenGenerator.REFRESH_TOKEN, TokenGenerator.generateToken(TokenGenerator.REFRESH_TOKEN, user.get().getId()));

        return user;
    }

    @PutMapping("/user/{id}")
    public CompletableFuture<User> updateLevel(@PathVariable int id) { return userService.updateLevel(id); }

}
