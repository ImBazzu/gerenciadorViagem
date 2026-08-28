package br.com.imbazzu.secretariaTransporte.trajeto.core.destino.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;

import java.util.UUID;

public class DestinoNaoEncontradoException extends EntidadeNaoEncontradoException {
    public DestinoNaoEncontradoException(UUID idDestino) {

        super("Destino não encontrado:  id " + idDestino);
    }
}
