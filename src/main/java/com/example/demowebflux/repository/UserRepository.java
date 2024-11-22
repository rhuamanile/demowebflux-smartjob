package com.example.demowebflux.repository;

import com.example.demowebflux.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends JpaRepository<User, UUID> {
    CompletableFuture<User> findByEmail(String email);
}
