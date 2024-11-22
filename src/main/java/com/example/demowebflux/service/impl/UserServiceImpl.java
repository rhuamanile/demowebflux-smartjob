package com.example.demowebflux.service.impl;

import com.example.demowebflux.entity.Phone;
import com.example.demowebflux.entity.User;
import com.example.demowebflux.repository.PhoneRepository;
import com.example.demowebflux.repository.UserRepository;
import com.example.demowebflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Value("${message.error.user}")
    private String userErrorMessage;

    @Override
    public Mono<ResponseEntity<Object>> createUser(User user) {
        return Mono.fromFuture(userRepository.findByEmail(user.getEmail()))
                .flatMap(existingUser -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMensaje(userErrorMessage);
                    return Mono.just(ResponseEntity.badRequest().body((Object) errorResponse));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    user.setId(UUID.randomUUID());
                    user.setCreatedDate(OffsetDateTime.now());
                    user.setModifiedDate(OffsetDateTime.now());
                    user.setLastLogin(OffsetDateTime.now());
                    return Mono.fromFuture(CompletableFuture.supplyAsync(() -> userRepository.save(user)))
                            .flatMap(savedUser -> {
                                List<Phone> phones = user.getPhones();
                                if (phones != null) {
                                    phones.forEach(phone -> {
                                        phone.setId(UUID.randomUUID());
                                        phone.setUser(savedUser);
                                    });
                                    return Mono.fromFuture(CompletableFuture.supplyAsync(() -> phoneRepository.saveAll(phones)))
                                            .then(Mono.just(savedUser));
                                }
                                return Mono.just(savedUser);
                            })
                            .map(savedUser -> {
                                UserResponse response = new UserResponse();
                                response.setId(savedUser.getId());
                                response.setCreatedDate(savedUser.getCreatedDate());
                                response.setModifiedDate(savedUser.getModifiedDate());
                                response.setLastLogin(savedUser.getLastLogin());
                                response.setToken(savedUser.getToken());
                                response.setInactive(savedUser.isInactive());
                                return ResponseEntity.status(201).body((Object) response);
                            });
                }));
    }
}
