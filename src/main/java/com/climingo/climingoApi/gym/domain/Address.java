package com.climingo.climingoApi.gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = true, length = 50)
    private String zipCode;
}
