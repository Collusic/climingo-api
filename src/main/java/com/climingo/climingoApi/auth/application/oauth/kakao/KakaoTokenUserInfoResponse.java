package com.climingo.climingoApi.auth.application.oauth.kakao;

import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenUserInfoResponse implements OAuth2UserInfoResponse {

    private String tokenType;
    private String accessToken;
    private String idToken;
    private Integer expiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
    private String scope;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put("token_type", tokenType);
        attributes.put("access_token", accessToken);
        attributes.put("id_token", idToken);
        attributes.put("expires_in", expiresIn);
        attributes.put("refresh_token", refreshToken);
        attributes.put("refresh_token_expires_in", refreshTokenExpiresIn);
        attributes.put("scope", scope);

        return attributes;
    }
}
