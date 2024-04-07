package com.climingo.climingoApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ClimingoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClimingoApiApplication.class, args);
    }

}
