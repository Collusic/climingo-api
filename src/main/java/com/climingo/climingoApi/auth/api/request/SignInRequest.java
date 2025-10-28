package com.climingo.climingoApi.auth.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInRequest {

    @JsonProperty("providerType")
    @Pattern(regexp = "^(kakao|apple)$", message = "providerType은 kakao와 apple만 유효합니다.")
    @NotNull
    @Schema(description ="provider 타입",example = "kakao or apple", required = true)
    private final String providerType;

    @JsonProperty("providerToken")
    @NotNull
    @Schema(description = "provider 유저 정보 조회용 토큰", example = "xxxxxx")
    private final String providerToken;

    @JsonCreator
    @Builder
    public SignInRequest(
            @JsonProperty("providerType") String providerType,
            @JsonProperty("providerToken") String providerToken
    ) {
        this.providerType = providerType;
        this.providerToken = providerToken;
    }
}