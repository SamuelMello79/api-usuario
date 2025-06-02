package dev.mello.apiusuario.infrastructure.exception;

import org.springframework.security.core.AuthenticationException;

public class UnathorizedException extends AuthenticationException {
    public UnathorizedException(String message) {
        super(message);
    }
}
