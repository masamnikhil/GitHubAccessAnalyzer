package com.app.GitHubAccessAnalyzer.service;

import com.app.GitHubAccessAnalyzer.DTOs.UserAccessReport;
import com.app.GitHubAccessAnalyzer.client.GitHubApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubApiClient gitHubApiClient;

        @Cacheable(value="accessReport", key="#org")
        public Flux<UserAccessReport> generateAccessReport(String org) {

            return gitHubApiClient.getRepositories(org)
                    .flatMap(repo ->
                            gitHubApiClient.getCollaborators(org, repo.getName())
                                    .map(c -> Map.entry(c.getLogin(), repo.getName()))
                    )
                    .groupBy(Map.Entry::getKey)
                    .flatMap(groupedFlux ->
                            groupedFlux
                                    .map(Map.Entry::getValue)
                                    .collectList()
                                    .map(list -> {
                                        UserAccessReport resp = new UserAccessReport();
                                        resp.setUser(groupedFlux.key());
                                        resp.setRepositories(list);
                                        return resp;
                                    })
                    );
        }

}