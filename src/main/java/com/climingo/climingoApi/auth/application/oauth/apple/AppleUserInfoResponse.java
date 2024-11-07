package com.climingo.climingoApi.auth.application.oauth.apple;

import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.auth.util.JwtUtil;
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
public class AppleUserInfoResponse implements OAuth2UserInfoResponse {

    private String sub;
    private String email;
    private String nickname;
    private String providerToken;
    private Map<String, Object> attributes;

    public AppleUserInfoResponse(AppleTokenInfoResponse appleTokenResponse) {
        var claims = JwtUtil.getClaims(appleTokenResponse.getIdToken());
        if (claims.containsKey("sub")) {
            this.sub = (String) claims.get("sub");
        }

        if (claims.containsKey("email")) {
            this.email = (String) claims.get("email");
        }

        if (claims.containsKey("name")) {
            this.nickname = (String) claims.get("name");
        }

        this.providerToken = appleTokenResponse.getRefreshToken();
    }

    public AppleUserInfoResponse(String providerToken, AppleValidateTokenResponse result) {
        var claims = JwtUtil.getClaims(result.getIdToken());

        if (claims.containsKey("sub")) {
            this.sub = (String) claims.get("sub");
        }

        if (claims.containsKey("email")) {
            this.email = (String) claims.get("email");
        }

        if (claims.containsKey("name")) {
            this.nickname = (String) claims.get("name");
        }

        this.providerToken = providerToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes != null) {
            return this.attributes;
        }

        this.attributes = new HashMap<>();
        this.attributes.put("authId", sub);
        this.attributes.put("providerType", "apple");
        this.attributes.put("email", email);
        this.attributes.put("nickname", nickname);
        this.attributes.put("providerToken", providerToken);

        return this.attributes;
    }
}