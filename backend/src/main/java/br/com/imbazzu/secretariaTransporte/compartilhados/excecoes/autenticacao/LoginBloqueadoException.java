package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.autenticacao;

public class LoginBloqueadoException extends RuntimeException {
    public LoginBloqueadoException(String message) {
        super(message);
    }
}
