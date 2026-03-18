package com.climingo.climingoApi.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalInfo {

    @JsonProperty("height")
    private Double height;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("armSpan")
    private Double armSpan;

    public PhysicalInfo merge(PhysicalInfo update) {
        return new PhysicalInfo(
                update.height != null ? update.height : this.height,
                update.weight != null ? update.weight : this.weight,
                update.armSpan != null ? update.armSpan : this.armSpan
        );
    }
}
