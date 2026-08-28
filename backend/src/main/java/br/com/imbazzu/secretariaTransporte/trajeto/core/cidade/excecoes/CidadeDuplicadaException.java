package br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.excecoes;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeDuplicadaException;

public class CidadeDuplicadaException extends EntidadeDuplicadaException {
    public CidadeDuplicadaException(String nome, String estado) {
        super("Cidade já cadastrada. Nome: " + nome + ", Estado: " + estado);
    }
}
