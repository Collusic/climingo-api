package com.climingo.climingoApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@PropertySource("classpath:application.yml")
public class ClimingoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClimingoApiApplication.class, args);
    }

}
