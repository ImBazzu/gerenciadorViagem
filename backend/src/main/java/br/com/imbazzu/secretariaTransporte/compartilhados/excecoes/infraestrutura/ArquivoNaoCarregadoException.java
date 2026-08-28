package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura;

public class ArquivoNaoCarregadoException extends RuntimeException {
    public ArquivoNaoCarregadoException(String message) {
        super(message);
    }
}
