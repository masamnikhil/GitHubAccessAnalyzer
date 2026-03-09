package com.app.GitHubAccessAnalyzer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "github")
@Data
public class GitHubProperties {

    private String baseUrl;
    private String token;
    private Integer concurrency;
}
