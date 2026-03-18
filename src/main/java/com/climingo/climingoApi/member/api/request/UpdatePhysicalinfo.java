package com.climingo.climingoApi.member.api.request;

import com.climingo.climingoApi.member.domain.PhysicalInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = UpdatePhysicalinfo.UpdatePhysicalinfoBuilder.class)
public class UpdatePhysicalinfo {

    @NotNull(message = "physicalInfo는 필수입니다")
    @Valid
    @JsonProperty("physicalInfo")
    private final PhysicalInfo physicalInfo;
}
