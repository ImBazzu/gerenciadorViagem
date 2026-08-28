package br.com.imbazzu.frontSpring.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
