package com.example.demowebflux.expose.web;

import com.example.demowebflux.security.JwtUtil;
import org.openapitools.api.GenerateTokenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class AuthDelegateImpl implements GenerateTokenApi {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<ResponseEntity<String>> generateToken(String username, String password, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(jwtUtil.generateToken(username, password)));
    }
}
