This Spring Boot application connects to GitHub, retrieves repositories and collaborators for a given organization, aggregates access details, and exposes a REST API that returns a structured JSON report.

## Authentication Configuration
1.Log in to your GitHub account.
2.Go to Profile → Settings → Developer Settings → Personal Access Tokens → Tokens (Classic) → Generate new token.
3.Give your token the following scopes:
4.repo → to access repositories
5.read:org → to read organization members and permissions
6.Add the token to .env file or system environment variables as described above.


## Setup and Configuration
1. Clone the project:
Open Git Bash or terminal and run:
git clone https://github.com/masamnikhil/GitHubAccessAnalyzer.git
cd GitHubAccessAnalyzer
2.Create a .env file in the project root (or set system environment variables) and add your GitHub token:
    - GITHUB_TOKEN=ghp_YourPersonalAccessTokenHere
    - Use the .env.example as a template.

To run the Application 
   - use mvn spring-boot:run if maven installed in your system or
   - use maven wrapper .\mvnw spring-boot:run   # Windows
                       ./mvnw spring-boot:run   # Linux/macOS

Application will run at port 8080.
Adjust concurrency to avoid hitting GitHub rate limits if your org is very large.

## calling endpoint
call the endpoint : http://localhost:8080/api/github/access-report/{org}

Output :
 [
  {
    "userLogin": "alice",
    "repositories": [
    {
        "repoName": "project-alpha",
        "permissions": {
          "admin": true,
          "maintain": true,
          "push": true,
          "triage": false,
          "pull": true
        },
        "roleName": "admin"
      },
      {
        "repoName": "project-beta",
        "permissions": {
          "admin": false,
          "maintain": false,
          "push": true,
          "triage": false,
          "pull": true
        },
        "roleName": "write"
      }
    ]
  },
  {
    "userLogin": "bob",
    "repositories": [
      {
        "repoName": "project-alpha",
        "permissions": {
          "admin": false,
          "maintain": false,
          "push": false,
          "triage": false,
          "pull": true
        },
        "roleName": "read"
      }
    ]
  }
]

Explanation
userLogin -> GitHub username
repositories -> List of repos the user has access to
permissions -> GitHub permissions flags (admin, push, pull, maintain, triage)
roleName -> GitHub role on that repository (admin, maintain, write, read)

## Assumptions & Design notes
Uses Spring WebFlux and Flux to efficiently handle 100+ repositories and 1000+ users.
Aggregates user access into UserAccessReport objects.
Includes collaborator permissions and role_name for each repository.
Errors from GitHub (401, 403, 404) are handled gracefully and returned with clear messages.
Authentication is via GitHub Personal Access Token, read securely from environment variables.
Caching included in project to improve performance of the application
