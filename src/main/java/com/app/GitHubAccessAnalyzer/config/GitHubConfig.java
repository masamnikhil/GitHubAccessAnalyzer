package com.app.GitHubAccessAnalyzer.config;

import com.app.GitHubAccessAnalyzer.properties.GitHubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class GitHubConfig {

    private final GitHubProperties props;

    @Bean
    public WebClient gitHubWebClient(){

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + props.getToken())
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }

}
