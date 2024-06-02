package com.climingo.climingoApi.message.error;

import com.climingo.climingoApi.message.DiscordClient;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ErrorAlertMessageProvider {

    @Value("${discord.webhook-url}")
    private String url;

    public void sendMessage(Exception e, HttpServletRequest request) {
        ErrorAlertMessage errorAlertMessage = createAlertMessage(e, request);
        sendMessageToDiscord(errorAlertMessage);
    }

    private ErrorAlertMessage createAlertMessage(Exception e, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .format(LocalDateTime.now());

        String originalDomain = request.getHeader("X-Forwarded-Host");
        if (originalDomain == null || originalDomain.isEmpty()) {
            originalDomain = request.getServerName();
        }

        // TODO POST 요청 데이터를 읽기
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line.replace("\"", "\\\""));
            }
        } catch (IOException ex) {
            // 요청 본문을 읽는데 실패한 경우, 예외 처리
            requestBody.append("Error reading request body: ").append(ex.getMessage());
        }

        return ErrorAlertMessage.builder()
            .requestTime(requestTime)
            .requestIp(originalDomain)
            .requestUrl("[" + request.getMethod() + "] " + request.getRequestURL())
            .requestQuery(request.getQueryString() == null ? "" : request.getQueryString())
            .requestData(requestBody.toString())
            .errorMessage(e.getMessage())
            .build();
    }

    private void sendMessageToDiscord(ErrorAlertMessage alertMessage) {
        DiscordClient discordClient = DiscordClient.getInstance();
        discordClient.send(url, alertMessage.toDiscordMessage());
    }
}
