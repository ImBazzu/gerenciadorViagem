package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais;

public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
