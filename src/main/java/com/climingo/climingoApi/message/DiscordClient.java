package com.climingo.climingoApi.message;

import static org.apache.commons.codec.CharEncoding.UTF_8;

import org.apache.http.entity.ContentType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class DiscordClient {
    private static final DiscordClient SINGLETON = new DiscordClient();

    private DiscordClient() {
    }

    public static DiscordClient getInstance() {
        return SINGLETON;
    }

    public void send(String url, DiscordMessage discordMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, String.valueOf(ContentType.APPLICATION_JSON));
            headers.add(HttpHeaders.ACCEPT_ENCODING, UTF_8);

            HttpEntity<String> requestEntity = new HttpEntity<>(discordMessage.toJson(), headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.POST,  requestEntity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
