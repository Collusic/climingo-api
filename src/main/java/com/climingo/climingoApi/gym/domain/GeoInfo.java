package com.climingo.climingoApi.gym.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
public class GeoInfo {

    private Double latitude;
    private Double longitude;
}
