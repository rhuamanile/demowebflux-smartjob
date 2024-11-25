package com.example.demowebflux.service;

import com.example.demowebflux.entity.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<ResponseEntity<Object>> createUser(User user);

}
