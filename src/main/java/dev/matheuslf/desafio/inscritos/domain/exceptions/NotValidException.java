package dev.matheuslf.desafio.inscritos.domain.exceptions;

public class NotValidException extends RuntimeException {
    public NotValidException(String message) {
        super(message);
    }
}
