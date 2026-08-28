package br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;

import java.util.UUID;

public class CidadeNaoEncontradoException extends EntidadeNaoEncontradoException {
    public CidadeNaoEncontradoException(UUID idCidade) {
        super("Cidade não encontrada: id " + idCidade);
    }
}
