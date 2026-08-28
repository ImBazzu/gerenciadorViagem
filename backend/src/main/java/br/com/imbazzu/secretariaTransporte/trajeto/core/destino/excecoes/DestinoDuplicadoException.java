package br.com.imbazzu.secretariaTransporte.trajeto.core.destino.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeDuplicadaException;

import java.util.UUID;

public class DestinoDuplicadoException extends EntidadeDuplicadaException {
    public DestinoDuplicadoException(UUID idCidade, String destinoNome) {

        super("Cidade já com Destino cadastrado. \nIdCidade: " +idCidade + "\nDestino: " +destinoNome);
    }
}
