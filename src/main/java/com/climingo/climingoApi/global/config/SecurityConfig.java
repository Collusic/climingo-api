package com.climingo.climingoApi.global.config;


import com.climingo.climingoApi.auth.application.TokenService;
import com.climingo.climingoApi.global.auth.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;

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
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/members", "/auth/members/exist").authenticated()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers("/healthCheck", "/sign-up", "/sign-in").permitAll()
                        .anyRequest().authenticated()
                )
            )
            .exceptionHandling((exceptionConfig) ->
                exceptionConfig
                    .authenticationEntryPoint(unauthorizedEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterAt(
                new JwtAuthenticationFilter(authenticationManager, tokenService),
                BasicAuthenticationFilter.class);

        return http.build();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
        (request, response, authException) -> {
            ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Spring security unauthorized...");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String json = new ObjectMapper().writeValueAsString(fail);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
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
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
        };

    public record ErrorResponse(HttpStatus status, String message) {
    }
}
