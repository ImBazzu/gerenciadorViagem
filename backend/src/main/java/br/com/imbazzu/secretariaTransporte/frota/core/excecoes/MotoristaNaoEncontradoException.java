package br.com.imbazzu.secretariaTransporte.frota.core.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;

import java.util.UUID;

public class MotoristaNaoEncontradoException extends EntidadeNaoEncontradoException {
    public MotoristaNaoEncontradoException(UUID id) {

        super("Motorista não encontrada. Id: " + id);
    }
}
