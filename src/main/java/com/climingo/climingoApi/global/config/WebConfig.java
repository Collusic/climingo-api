package com.climingo.climingoApi.global.config;

import com.climingo.climingoApi.global.auth.LoginMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOrigins(
                "http://localhost:3000",
                "https://localhost:3000",
                "https://app.climingo.xyz",
                "https://stg-app.climingo.xyz",
                "https://dev-app.climingo.xyz",
                "https://local.app.climingo.xyz:3000",
                "https://local.stg-app.climingo.xyz:3000",
                "https://local.dev-app.climingo.xyz:3000")
            .allowedMethods("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
