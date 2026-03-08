package com.app.GitHubAccessAnalyzer.exceptionHandler;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) { super(message); }
}
