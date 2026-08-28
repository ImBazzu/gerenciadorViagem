package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class EntidadeEmUsoException extends RuntimeException {
    public EntidadeEmUsoException(String message) {
        super(message);
    }
}
