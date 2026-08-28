package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.excecoes;

public class PessoaDuplicadaException extends RuntimeException {
    public PessoaDuplicadaException(String cpf) {

        super("Pessoa já cadastrado. CPF: "+ cpf);
    }
}
