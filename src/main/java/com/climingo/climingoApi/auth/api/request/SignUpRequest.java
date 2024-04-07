package com.climingo.climingoApi.auth.api.request;

import com.climingo.climingoApi.member.domain.PhysicalInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = SignUpRequest.SignUpRequestBuilder.class)
public class SignUpRequest {

    @JsonProperty("providerType")
    @Pattern(regexp = "^(kakao|apple)$", message = "providerType은 kakao와 apple만 유효합니다.")
    @NotNull
    private final String providerType;

    @JsonProperty("authId")
    @NotNull
    private final String authId;

    @JsonProperty("providerToken")
    @NotNull
    private final String providerToken;

    @JsonProperty("nickname")
    @Size(min = 2, max = 8)
    private final String nickname;

    @JsonProperty("profileImage")
    private final String profileImage;

    @JsonProperty("homeGymId")
    private final Long homeGymId;

    @JsonProperty("physicalInfo")
    private final PhysicalInfo physicalInfo;
}
