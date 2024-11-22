package com.example.demowebflux.expose.web;

import com.example.demowebflux.entity.Phone;
import com.example.demowebflux.exception.UserAlreadyExistsException;
import com.example.demowebflux.entity.User;
import com.example.demowebflux.service.UserService;
import org.openapitools.api.UsersApi;
import org.openapitools.model.CreateUser201Response;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class UserDelegateImpl implements UsersApi {

    @Autowired
    private UserService userService;


    @Override
    public Mono<ResponseEntity<CreateUser201Response>> createUser(Mono<UserRequest> userRequest, ServerWebExchange exchange) {
        return userRequest.flatMap(request -> {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setCreatedDate(OffsetDateTime.now());
            user.setModifiedDate(OffsetDateTime.now());
            user.setLastLogin(OffsetDateTime.now());
            user.setToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            user.setInactive(true);

            List<Phone> phones = request.getPhones().stream().map(phoneRequest -> {
                Phone phone = new Phone();
                phone.setNumber(phoneRequest.getNumber());
                phone.setCountry_code(phoneRequest.getContrycode());
                phone.setUser(user);
               return phone;
            }).collect(Collectors.toList());

            user.setPhones(phones);

            return userService.createUser(user)
                    .map(response -> ResponseEntity.status(response.getStatusCode()).body((CreateUser201Response) response.getBody()))
                    .onErrorResume(UserAlreadyExistsException.class, e -> {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.setMensaje(e.getMessage());
                        return Mono.just(ResponseEntity.badRequest().body((CreateUser201Response) errorResponse));
                    });
        });
    }
}
