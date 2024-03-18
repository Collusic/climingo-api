package com.climingo.climingoApi.healthCheck.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/healthCheck")
    private ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }

}
