package com.app.GitHubAccessAnalyzer.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permissions {

    private boolean admin;
    private boolean maintain;
    private boolean push;
    private boolean triage;
    private boolean pull;
}
