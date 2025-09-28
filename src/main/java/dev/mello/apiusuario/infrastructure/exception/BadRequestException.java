package dev.mello.apiusuario.infrastructure.exception;

public class BadRequestException extends RuntimeException {
    private static final long servialVersionUID = 1L;
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
