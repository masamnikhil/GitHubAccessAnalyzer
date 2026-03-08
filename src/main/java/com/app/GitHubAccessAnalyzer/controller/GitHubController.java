package com.app.GitHubAccessAnalyzer.controller;

import com.app.GitHubAccessAnalyzer.DTOs.UserAccessReport;
import com.app.GitHubAccessAnalyzer.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/access-report/{org}")
    public Flux<UserAccessReport> getAccessReport(@PathVariable(name = "org") String organization){
        return gitHubService.generateAccessReport(organization);
    }
}
