package com.climingo.climingoApi.gym.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String address;
    private String zipCode;
}
