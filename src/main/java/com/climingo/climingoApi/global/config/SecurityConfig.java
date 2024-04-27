package com.climingo.climingoApi.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .headers((headerConfig) -> headerConfig.frameOptions(FrameOptionsConfig::disable))
            .authorizeHttpRequests((authorizeRequests ->
                    // TODO 어떤 API에 인증/인가 처리를 둘 것인지 논의 필요
                    authorizeRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers("/healthCheck", "/sign-up", "/sign-in", "/auth/members/exist").permitAll()
                        .anyRequest().authenticated()
                )
            )
            .exceptionHandling((exceptionConfig) ->
                exceptionConfig
                    .authenticationEntryPoint(unauthorizedEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
        (request, response, authException) -> {
            ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Spring security unauthorized...");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String json = new ObjectMapper().writeValueAsString(fail);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        };

    private final AccessDeniedHandler accessDeniedHandler =
        (request, response, accessDeniedException) -> {
            ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN,
                "Spring security forbidden...");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            String json = new ObjectMapper().writeValueAsString(fail);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        };

    public record ErrorResponse(HttpStatus status, String message) {
    }
}
