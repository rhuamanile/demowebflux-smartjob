package com.example.demowebflux.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue
    private UUID id;
    private String number;
    private String city_code;
    private String country_code;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }
}
