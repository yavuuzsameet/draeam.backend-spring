package dev.xamet.dreamgamesstudy.repository;

import dev.xamet.dreamgamesstudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> { }
