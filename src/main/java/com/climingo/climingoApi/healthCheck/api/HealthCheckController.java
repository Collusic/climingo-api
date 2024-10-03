package com.climingo.climingoApi.healthCheck.api;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.build.time}")
    private String buildTime;

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> baseUrl() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/healthCheck");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/healthCheck")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "ok");
        data.put("app version", appVersion);
        data.put("last build", buildTime);

        return ResponseEntity.ok(data);
    }

}
