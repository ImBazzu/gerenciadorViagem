package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura;

public class BancoDeDadosException extends RuntimeException {
    public BancoDeDadosException(String message) {
        super("Regra de banco de dados infligida \n" + message);
    }

    public BancoDeDadosException(String message, Throwable cause) {
        super(message, cause);
    }
}
