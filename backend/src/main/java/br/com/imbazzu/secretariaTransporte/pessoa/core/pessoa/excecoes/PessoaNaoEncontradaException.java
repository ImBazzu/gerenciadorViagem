package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.excecoes;

import java.util.UUID;

public class PessoaNaoEncontradaException extends RuntimeException {
    public PessoaNaoEncontradaException(UUID idPessoa) {
        super("Pessoa não encontrada. Id: " + idPessoa);
    }
}
