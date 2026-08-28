package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }
}
