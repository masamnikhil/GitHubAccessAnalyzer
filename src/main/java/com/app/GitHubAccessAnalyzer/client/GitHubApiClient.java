package com.app.GitHubAccessAnalyzer.client;

import com.app.GitHubAccessAnalyzer.DTOs.CollaboratorResponse;
import com.app.GitHubAccessAnalyzer.DTOs.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
                                        .retrieve()
                                        .bodyToFlux(RepositoryResponse.class)
                                        .collectList()
                                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))

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
                                        .retrieve()
                                        .bodyToFlux(CollaboratorResponse.class)
                                        .collectList()

                                        .retryWhen(
                                                Retry.backoff(3, Duration.ofSeconds(1))
                                        )

                                        .onErrorResume(e -> Mono.just(List.of()))

                        , 5)

                .takeUntil(List::isEmpty)

                .flatMapIterable(list -> list);
    }
}
