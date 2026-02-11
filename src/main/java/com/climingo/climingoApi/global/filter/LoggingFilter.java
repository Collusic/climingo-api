package com.climingo.climingoApi.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final String MDC_KEY_TRACE_ID = "traceId";
    private static final String[] LOG_BODY_METHODS = { "POST", "PUT", "PATCH" };
    private static final String[] HEADER_KEY_LIST = { "Authorization", "transactionUniqueNum" }; // 내가 원하는 header key만 출력되도록 설정
    private static final String[] URI_WITHOUT_LOG = { "swagger", "api-docs", "healthcheck" };
    private static final int MAX_BODY_LOG_LENGTH = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString();
        MDC.put(MDC_KEY_TRACE_ID, traceId);

        try {
            if (isHealthcheckOrSwaggerRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            long startTime = System.currentTimeMillis();

            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

            // check authorization header exists
            logRequest(wrappedRequest);

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            logResponse(wrappedResponse, elapsedTime);

            wrappedResponse.copyBodyToResponse();
        } finally {
            MDC.remove(MDC_KEY_TRACE_ID);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        logRequestBasicInfo(request);
        logRequestTargetHeaders(request);

        boolean isMethodAllowed = Arrays.stream(LOG_BODY_METHODS)
                                        .anyMatch(target -> target.equalsIgnoreCase(request.getMethod()));

        if (!isMethodAllowed) {
            return;
        }
        String body = getRequestMessageBody(request);
        filterAndLogBody(body);
    }

    private void logResponse(ContentCachingResponseWrapper wrappedResponse, long elapsedTime) {
        log.info("[RESPONSE] Status : {}, Processing Time : {}ms", wrappedResponse.getStatus(),
                 elapsedTime);

        logResponseHeaders(wrappedResponse);
    }

    private void logRequestBasicInfo(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String queryString = request.getQueryString();

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        log.info("[REQUEST] Method: {}, URI: {}, Client-IP: {}, User-Agent: {}, QueryString: {}, PathVariables: {}", method, uri, clientIp, userAgent,
                 (queryString != null ? queryString : ""), (pathVariables != null ? pathVariables : "{}"));
    }

    private void logRequestTargetHeaders(ContentCachingRequestWrapper wrappedRequest) {
        for (String headerKey : HEADER_KEY_LIST) {
            String headerValue = wrappedRequest.getHeader(headerKey);
            if (headerValue != null) {
                log.info("[REQUEST] {} : {}", headerKey, headerValue);
            }
        }
    }

    private void logResponseHeaders(ContentCachingResponseWrapper wrappedResponse) {
        // process same key on header
        HashSet<String> headerKeySet = new HashSet<>(wrappedResponse.getHeaderNames());

        // log specific header value
        headerKeySet.forEach(key -> {
            HashSet<String> valueSet = new HashSet<>(wrappedResponse.getHeaders(key));
            valueSet.forEach(value -> {
                log.info("[RESPONSE] Header : {}={}", key, value);
            });
        });
    }

    private String getRequestMessageBody(ContentCachingRequestWrapper wrappedRequest) {
        return truncateBody(new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    private String truncateBody(String body) {
        if (body.length() > MAX_BODY_LOG_LENGTH) {
            return body.substring(0, MAX_BODY_LOG_LENGTH) + " [TRUNCATED]";
        }
        return body;
    }

    private void filterAndLogBody(String messageBody) {
        if (messageBody.isEmpty()) {
            return;
        }

        log.info("[REQUEST] Body : {}", messageBody);
    }

    private boolean isHealthcheckOrSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return Arrays.stream(URI_WITHOUT_LOG).anyMatch(uri::contains);
    }

}
