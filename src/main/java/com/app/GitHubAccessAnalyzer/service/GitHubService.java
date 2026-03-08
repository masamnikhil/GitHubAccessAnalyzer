package com.app.GitHubAccessAnalyzer.service;

import com.app.GitHubAccessAnalyzer.DTOs.AccessDetail;
import com.app.GitHubAccessAnalyzer.DTOs.UserAccessReport;
import com.app.GitHubAccessAnalyzer.client.GitHubApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubApiClient gitHubApiClient;

        @Cacheable(value="accessReport", key="#org")
        public Flux<UserAccessReport> generateAccessReport(String org) {

            Map<String, List<AccessDetail>> userAccessMap = new ConcurrentHashMap<>();

            return gitHubApiClient.getRepositories(org)
                    .flatMap(repo ->
                                    gitHubApiClient.getCollaborators(org, repo.getName())
                                            .doOnNext(collaborator -> {
                                                userAccessMap
                                                        .computeIfAbsent(collaborator.getLogin(), k -> new CopyOnWriteArrayList<>())
                                                        .add(new AccessDetail(
                                                                repo.getName(),
                                                                collaborator.getPermissions(),
                                                                collaborator.getRoleName()
                                                        ));
                                            })
                            , 5)
                    .thenMany(
                            Flux.fromIterable(userAccessMap.entrySet())
                                    .map(entry -> new UserAccessReport(entry.getKey(), entry.getValue()))
                    );
        }
}