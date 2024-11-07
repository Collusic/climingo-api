package com.climingo.climingoApi.auth.application.oauth.apple;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleValidateTokenResponse {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String idToken;
}
