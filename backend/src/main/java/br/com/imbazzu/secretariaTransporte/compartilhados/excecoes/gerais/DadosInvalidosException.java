package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class DadosInvalidosException extends RuntimeException {
    public DadosInvalidosException(String message) {
        super(message);
    }
}
