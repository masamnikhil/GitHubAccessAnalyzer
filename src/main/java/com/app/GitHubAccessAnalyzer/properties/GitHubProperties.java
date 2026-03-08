package com.app.GitHubAccessAnalyzer.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "github")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubProperties {

    private String baseUrl;
    private String token;
}
