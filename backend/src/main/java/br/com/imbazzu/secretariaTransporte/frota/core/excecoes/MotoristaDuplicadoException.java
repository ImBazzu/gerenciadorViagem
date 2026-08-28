package br.com.imbazzu.secretariaTransporte.frota.core.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeDuplicadaException;

public class MotoristaDuplicadoException extends EntidadeDuplicadaException {
    public MotoristaDuplicadoException(String cpf) {

        super("Motorista já cadastrado. CPF: "+ cpf);
    }
}
