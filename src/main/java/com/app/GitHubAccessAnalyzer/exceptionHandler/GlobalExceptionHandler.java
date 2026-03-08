package com.app.GitHubAccessAnalyzer.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleUnauthorized(UnauthorizedException ex) {
        Map<String, String> error = Map.of(
                "error", "Unauthorized",
                "message", ex.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRateLimit(RateLimitExceededException ex) {
        Map<String, Object> error = Map.of(
                "error", "Rate Limit Exceeded",
                "message", ex.getMessage(),
                "retryAfterSeconds", ex.getRetryAfterSeconds()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error));
    }

    @ExceptionHandler(ForbiddenException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleForbidden(ForbiddenException ex) {
        Map<String, String> error = Map.of(
                "error", "Forbidden",
                "message", ex.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneric(Exception ex) {
        Map<String, String> error = Map.of(
                "error", "Internal Server Error",
                "message", ex.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
