package com.climingo.climingoApi.auth.application.oauth.apple;

import com.climingo.climingoApi.auth.application.oauth.OAuth2ClientService;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.auth.util.JwtUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier(value = "AppleClientService")
@RequiredArgsConstructor
public class AppleClientService implements OAuth2ClientService {

    private final AppleTokenApi appleTokenApi;

    private final AppleValidateTokenApi appleValidateTokenApi;

    @Value("${oauth2.apple.client-id}")
    private String CLIENT_ID;

    @Value("${oauth2.apple.key.id}")
    private String KEY_ID;

    @Value("${oauth2.apple.team-id}")
    private String TEAM_ID;

    @Value("${oauth2.apple.key.path}")
    private String PRIVATE_KEY;

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String GRANT_TYPE = "authorization_code";

    private static final String AUDIENCE = "https://appleid.apple.com";

    @Override
    public OAuth2UserInfoResponse requestAuth(String code, String redirectUri) {
        AppleTokenInfoResponse appleTokenResponse = appleTokenApi.requestToken(
            CONTENT_TYPE, GRANT_TYPE, generateClientSecret(), CLIENT_ID, redirectUri, code);

        return new AppleUserInfoResponse(appleTokenResponse);
    }

    @Override
    public OAuth2UserInfoResponse requestAuth(String providerToken) {
        var result = appleValidateTokenApi.validateToken(
            CLIENT_ID, "refresh_token", generateClientSecret(), providerToken);

        return new AppleUserInfoResponse(providerToken, result);
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, KEY_ID)
            .setIssuer(TEAM_ID)
            .setAudience(AUDIENCE)
            .setSubject(CLIENT_ID)
            .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.ES256, getPrivateKey())
            .compact();
    }

    private PrivateKey getPrivateKey() {

        Security.addProvider(new BouncyCastleProvider());
        var converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
}
