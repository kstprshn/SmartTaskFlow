package ru.java.teamProject.SmartTaskFlow.util;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
