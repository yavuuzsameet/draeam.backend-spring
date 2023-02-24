package dev.xamet.dreamgamesstudy.service;

import dev.xamet.dreamgamesstudy.entity.User;
import dev.xamet.dreamgamesstudy.exception.NotFoundException;
import dev.xamet.dreamgamesstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class UserService {

    /*
     * Injection
     */
    @Autowired
    private UserRepository userRepository;

    /*
     * Creates a new user with level 1 and 5000 coins
     */
    @Async
    public CompletableFuture<User> createUser() {

        User user = new User();

        user.setLevel(1);
        user.setCoins(5000);

        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    /*
     * Updates the level of the user by 1 and adds 25 coins
     */
    @Async
    public CompletableFuture<User> updateLevel(int id) {

        User user = getUserById(id);

        user.setLevel(user.getLevel() + 1);
        user.setCoins(user.getCoins() + 25);

        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    /*
     * Returns the user with the given id
     */
    private User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found with id: " + id)
        );
    }
}
