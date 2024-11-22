package com.example.demowebflux.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/users/**"));
        setServerAuthenticationConverter(this::convert);
        setAuthenticationFailureHandler((webFilterExchange, exception) -> {
            ServerWebExchange exchange = webFilterExchange.getExchange();
            return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
        });
    }

    private Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String password = jwtUtil.getPasswordFromToken(token);
                if (username != null && password != null) {
                    User user = new User(username, password, Collections.emptyList());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
                    return Mono.just(authentication);
                }
            }
        }
        return Mono.empty();
    }
}