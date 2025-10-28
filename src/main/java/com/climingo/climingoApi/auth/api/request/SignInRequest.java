package com.climingo.climingoApi.auth.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = SignInRequest.SignInRequestBuilder.class)
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
}