package br.com.imbazzu.frontSpring.exception;

public class AuthenticationExpiredException extends RuntimeException {
    public AuthenticationExpiredException() {
        super("Sessão expirada. Faça Login novamente.");
    }
}
