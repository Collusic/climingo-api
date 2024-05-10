package com.climingo.climingoApi.gym.api.response;

import lombok.Getter;

@Getter
public class GymSearchResponse {

    private Long id;
    private String address;
    private String zipCode;
    private String name;

    public GymSearchResponse(Long id, String address, String zipCode, String name) {
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
        this.name = name;
    }

}