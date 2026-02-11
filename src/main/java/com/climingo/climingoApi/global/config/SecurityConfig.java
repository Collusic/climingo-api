package com.climingo.climingoApi.global.config;


import com.climingo.climingoApi.auth.application.AuthTokenService;
import com.climingo.climingoApi.global.auth.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthTokenService authTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(
            AuthenticationManagerBuilder.class).build();

        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .headers((headerConfig) -> headerConfig.frameOptions(FrameOptionsConfig::disable))
            .authenticationManager(authenticationManager)
            .authorizeHttpRequests((authorizeRequests ->
                    authorizeRequests
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/members", "/myRecords").authenticated()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers("/healthCheck", "/sign-up", "/sign-in", "/auth/members/exist").permitAll()
                        .anyRequest().authenticated()
                )
            )
            .exceptionHandling((exceptionConfig) ->
                exceptionConfig
                    .authenticationEntryPoint(unauthorizedEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterAt(
                new JwtAuthenticationFilter(authenticationManager, authTokenService),
                BasicAuthenticationFilter.class);

        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
        (request, response, authException) -> {
            log.debug("[unauthorized]: {}", authException.getMessage());
            ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Spring security unauthorized...");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String json = new ObjectMapper().writeValueAsString(fail);
            setCorsHeaders(request, response);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        };

    private final AccessDeniedHandler accessDeniedHandler =
        (request, response, accessDeniedException) -> {
            log.debug("[accessDenied]: {}", accessDeniedException.getMessage());
            ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN,
                "Spring security forbidden...");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            String json = new ObjectMapper().writeValueAsString(fail);
            setCorsHeaders(request, response);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        };

    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    public record ErrorResponse(HttpStatus status, String message) {
    }
}
