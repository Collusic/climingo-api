package com.climingo.climingoApi.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final String MDC_KEY_TRACE_ID = "traceId";
    private static final String[] LOG_BODY_METHODS = {"POST", "PUT", "PATCH"};
    private static final String[] HEADER_KEY_LIST = {"Authorization", "transactionUniqueNum"};
    private static final String[] URI_WITHOUT_LOG = {"swagger", "api-docs", "healthcheck"};

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
        String method = request.getMethod();

        log.info("[REQUEST] {} {}", method, request.getRequestURI());

        logRequestTargetHeaders(request);

        logRequestQueryString(request);

        boolean isMethodAllowed = Arrays.stream(LOG_BODY_METHODS)
            .anyMatch(target -> target.equalsIgnoreCase(method));

        if (!isMethodAllowed) {
            return;
        }
        String body = getRequestMessageBody(request);
        filterAndLogBody(body, true);
    }

    private void logResponse(ContentCachingResponseWrapper wrappedResponse, long elapsedTime) {
        log.info("[RESPONSE] Status : {}, Processing Time : {}ms", wrappedResponse.getStatus(),
            elapsedTime);

        logResponseHeaders(wrappedResponse);

        String responseBody = getResponseMessageBody(wrappedResponse);

        filterAndLogBody(responseBody, false);
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

    private static void logRequestQueryString(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        if (queryString != null) {
            log.info("[REQUEST] QueryString : {}", queryString);
        }
    }

    private String getRequestMessageBody(ContentCachingRequestWrapper wrappedRequest) {
        return new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private String getResponseMessageBody(ContentCachingResponseWrapper wrappedResponse) {
        return new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private void filterAndLogBody(String messageBody, boolean isRequest) {
        if (messageBody.isEmpty()) {
            return;
        }

        log.info("[{}] Body : {}", isRequest ? "REQUEST" : "RESPONSE", messageBody);
    }

    private boolean isHealthcheckOrSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return Arrays.stream(URI_WITHOUT_LOG).anyMatch(uri::contains);
    }
}
