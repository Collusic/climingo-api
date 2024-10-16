package com.climingo.climingoApi.global.config;

import com.climingo.climingoApi.global.auth.RequestMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    private final RequestMemberArgumentResolver requestMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestMemberArgumentResolver);
    }
}