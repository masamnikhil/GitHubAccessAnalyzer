package com.app.GitHubAccessAnalyzer.client;

import com.app.GitHubAccessAnalyzer.DTOs.CollaboratorResponse;
import com.app.GitHubAccessAnalyzer.DTOs.RepositoryResponse;
import com.app.GitHubAccessAnalyzer.exceptionHandler.ForbiddenException;
import com.app.GitHubAccessAnalyzer.exceptionHandler.RateLimitExceededException;
import com.app.GitHubAccessAnalyzer.exceptionHandler.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GitHubApiClient {

    private final WebClient gitHubWebClient;

    public Flux<RepositoryResponse> getRepositories(String org) {

        return Flux.range(1, 500)

                .flatMapSequential(page ->

                                gitHubWebClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                .path("/orgs/{org}/repos")
                                                .queryParam("per_page", 100)
                                                .queryParam("page", page)
                                                .build(org))
                                        .exchangeToFlux(response -> handleResponse(response, RepositoryResponse.class))
                                        .collectList()
                                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                                                .filter(ex -> !(ex instanceof UnauthorizedException || ex instanceof RateLimitExceededException))
                                        )

                        , 5)
                .takeUntil(List::isEmpty)
                .flatMapIterable(list -> list);
    }


    public Flux<CollaboratorResponse> getCollaborators(String org, String repo) {

        return Flux.range(1, 1000)

                .flatMapSequential(page ->

                                gitHubWebClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                .path("/repos/{org}/{repo}/collaborators")
                                                .queryParam("per_page", 100)
                                                .queryParam("page", page)
                                                .build(org, repo))
                                        .exchangeToFlux(response -> handleResponse(response, CollaboratorResponse.class))
                                        .collectList()

                                        .retryWhen(
                                                Retry.backoff(3, Duration.ofSeconds(1))
                                                        .filter(ex -> !(ex instanceof UnauthorizedException || ex instanceof RateLimitExceededException))
                ), 5)

                .takeUntil(List::isEmpty)
                .flatMapIterable(list -> list);
    }

    private <T> Flux<T> handleResponse(ClientResponse response, Class<T> responseClass) {
        if (response.statusCode().value() == 401) {
            return Flux.error(new UnauthorizedException("Unauthorized: Check GitHub token"));
        } else if (response.statusCode().value() == 403) {
            String reset = response.headers().asHttpHeaders().getFirst("X-RateLimit-Reset");
            if (reset != null) {
                long waitSeconds = Long.parseLong(reset) - (System.currentTimeMillis() / 1000);
                return Flux.error(new RateLimitExceededException("Rate limit exceeded", waitSeconds));
            }
            return Flux.error(new ForbiddenException("Token does not have permission"));
        } else if (response.statusCode().value() == 403) {
            return Flux.empty();
        }
        else if (response.statusCode().is5xxServerError()) {
            return Flux.error(new RuntimeException("GitHub server error: " + response.statusCode()));
        }
        return response.bodyToFlux(responseClass);
    }
}
