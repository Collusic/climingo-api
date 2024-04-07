package com.climingo.climingoApi.auth.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = SignInRequest.SignInRequestBuilder.class)
public class SignInRequest {

    @JsonProperty("providerType")
    private final String providerType;

    @JsonProperty("providerToken")
    private final String providerToken;
}