package com.climingo.climingoApi.auth.api.request;

import com.climingo.climingoApi.member.domain.PhysicalInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
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
    @Size(min = 2, max = 16)
    private final String nickname;

    @JsonProperty("profileUrl")
    private final String profileUrl;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("homeGymId")
    private final Long homeGymId;

    @JsonProperty("physicalInfo")
    private final PhysicalInfo physicalInfo;

    @JsonCreator
    @Builder
    public SignUpRequest(
            @JsonProperty("providerType") String providerType,
            @JsonProperty("authId") String authId,
            @JsonProperty("providerToken") String providerToken,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("profileUrl") String profileUrl,
            @JsonProperty("email") String email,
            @JsonProperty("homeGymId") Long homeGymId,
            @JsonProperty("physicalInfo") PhysicalInfo physicalInfo
    ) {
        this.providerType = providerType;
        this.authId = authId;
        this.providerToken = providerToken;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
        this.homeGymId = homeGymId;
        this.physicalInfo = physicalInfo;
    }
}
