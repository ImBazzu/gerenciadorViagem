package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.excecoes;

public class PessoaCondicaoDuplicadaException extends RuntimeException {
    public PessoaCondicaoDuplicadaException(String nomeCondicao) {

        super("Condição já cadastrada. Nome:" +nomeCondicao);
    }
}
