package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.autenticacao;

public class UsuarioNaoAutorizadoException extends RuntimeException {
    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }
}
