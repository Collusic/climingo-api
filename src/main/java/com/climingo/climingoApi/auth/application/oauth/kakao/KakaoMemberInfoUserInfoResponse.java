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
public class KakaoMemberInfoUserInfoResponse implements OAuth2UserInfoResponse {

    private String sub;
    private String email;
    private String nickname;
    private String picture;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes != null) {
            return this.attributes;
        }

        this.attributes = new HashMap<>();
        this.attributes.put("authId", sub);
        this.attributes.put("providerType", "kakao");
        this.attributes.put("email", email);
        this.attributes.put("picture", picture); // 기본값: 앱 연결 시의 카카오계정 썸네일 프로필 사진 URL, 110px*110px 크기
        this.attributes.put("nickname", nickname);

        return this.attributes;
    }
}