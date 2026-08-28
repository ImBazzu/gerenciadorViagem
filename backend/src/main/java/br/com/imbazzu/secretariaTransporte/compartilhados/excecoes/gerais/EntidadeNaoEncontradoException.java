package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class EntidadeNaoEncontradoException extends RuntimeException {
    public EntidadeNaoEncontradoException(String message) {
        super(message);
    }
}
