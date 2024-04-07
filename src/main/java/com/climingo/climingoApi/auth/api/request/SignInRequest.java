package com.climingo.climingoApi.auth.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
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
    private final String providerType;

    @JsonProperty("providerToken")
    @NotNull
    private final String providerToken;
}