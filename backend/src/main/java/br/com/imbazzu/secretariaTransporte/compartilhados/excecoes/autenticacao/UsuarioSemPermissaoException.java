package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.autenticacao;

public class UsuarioSemPermissaoException extends RuntimeException {
    public UsuarioSemPermissaoException(String message) {
        super(message);
    }
}
