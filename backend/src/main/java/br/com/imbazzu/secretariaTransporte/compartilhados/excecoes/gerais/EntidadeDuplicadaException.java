package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class EntidadeDuplicadaException extends RuntimeException {
    public EntidadeDuplicadaException(String message) {
        super(message);
    }
}
