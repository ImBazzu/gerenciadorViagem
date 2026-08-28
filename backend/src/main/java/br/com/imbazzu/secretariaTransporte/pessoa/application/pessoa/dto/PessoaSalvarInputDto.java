package br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto;

import java.util.UUID;

public record PessoaSalvarInputDto(String nome, String cpf,UUID idCondicao, String telefone,
                                   String endereco,String observacao ) {
}
