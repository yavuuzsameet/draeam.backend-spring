package dev.xamet.dreamgamesstudy.service;

import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.exception.NotFoundException;
import dev.xamet.dreamgamesstudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .level(1)
                .coins(5000)
                .build();
    }

    @Test
    void canCreateAUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        CompletableFuture<User> userCompletableFuture = userService.createUser();

        assertEquals(user.getId(), userCompletableFuture.join().getId());
        assertEquals(user.getLevel(), userCompletableFuture.join().getLevel());
        assertEquals(user.getCoins(), userCompletableFuture.join().getCoins());
    }

    @Test
    void canUpdateLevelOfAUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        CompletableFuture<User> userCompletableFuture = userService.updateLevel(user.getId());

        assertEquals(user.getId(), userCompletableFuture.join().getId());
        assertEquals(user.getLevel(), userCompletableFuture.join().getLevel());
        assertEquals(user.getCoins(), userCompletableFuture.join().getCoins());
    }

    @Test
    void canThrowExceptionWhenUserDoesNotExist() {

        Throwable exception = assertThrows(NotFoundException.class, () -> {
            userService.updateLevel(user.getId());
        });
        assertEquals("User not found with id: " + user.getId(), exception.getMessage());
    }
}