package com.climingo.climingoApi.message;

import static org.apache.commons.codec.CharEncoding.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(HttpHeaders.ACCEPT_ENCODING, UTF_8);

            // ObjectMapper를 사용하여 DiscordMessage 객체를 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(discordMessage);

            // HttpEntity 객체 생성
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonMessage, headers);


//            HttpEntity<DiscordMessage> requestEntity = new HttpEntity<>(discordMessage, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.POST,  requestEntity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
