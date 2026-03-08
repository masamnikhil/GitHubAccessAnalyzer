package com.app.GitHubAccessAnalyzer.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollaboratorResponse {

    private String login;

    private Permissions permissions;

    @JsonProperty("role_name")
    private String roleName;

}
