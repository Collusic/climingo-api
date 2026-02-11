package com.climingo.climingoApi.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalInfo {

    @JsonProperty("height")
    private BigDecimal height;

    @JsonProperty("weight")
    private BigDecimal weight;

    @JsonProperty("armSpan")
    private BigDecimal armSpan;
}
