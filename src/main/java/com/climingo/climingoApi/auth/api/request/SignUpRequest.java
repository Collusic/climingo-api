package com.climingo.climingoApi.auth.api.request;

import com.climingo.climingoApi.member.domain.PhysicalInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = SignUpRequest.SignUpRequestBuilder.class)
public class SignUpRequest {

    @JsonProperty("providerType")
    private final String providerType;

    @JsonProperty("authId")
    private final String authId;

    @JsonProperty("providerToken")
    private final String providerToken;

    @JsonProperty("nickname")
    private final String nickname;

    @JsonProperty("profileImage")
    private final String profileImage;

    @JsonProperty("homeGymId")
    private final Long homeGymId;

    @JsonProperty("physicalInfo")
    private final PhysicalInfo physicalInfo;
}
