package com.app.GitHubAccessAnalyzer.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessDetail {

    private String repoName;
    private Permissions permissions;
    private String roleName;

}
