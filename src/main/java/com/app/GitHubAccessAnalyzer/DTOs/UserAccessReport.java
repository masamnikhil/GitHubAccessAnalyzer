package com.app.GitHubAccessAnalyzer.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessReport {

    private String user;
    private List<String> repositories;
}
