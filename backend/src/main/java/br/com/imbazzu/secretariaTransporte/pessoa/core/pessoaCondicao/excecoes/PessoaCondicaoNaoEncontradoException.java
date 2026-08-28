package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.excecoes;

import java.util.UUID;

public class PessoaCondicaoNaoEncontradoException extends RuntimeException {
    public PessoaCondicaoNaoEncontradoException(UUID id) {

        super("Pessoa condição não encontrada. id: "+id);
    }
}
