package com.app.GitHubAccessAnalyzer.exceptionHandler;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException(String message) { super(message); }
}
