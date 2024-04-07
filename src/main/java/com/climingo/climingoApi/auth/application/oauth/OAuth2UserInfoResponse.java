package com.climingo.climingoApi.auth.application.oauth;

import java.util.Map;

public interface OAuth2UserInfoResponse {

    Map<String, Object> getAttributes();
}
