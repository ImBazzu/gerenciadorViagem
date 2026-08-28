package br.com.imbazzu.frontSpring.exception;

public class FormularioError extends RuntimeException {
    public FormularioError(String message) {
        super(message);
    }

}
