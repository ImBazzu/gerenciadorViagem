package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto;

import java.util.UUID;

public record PessoaResponseDto(UUID id,
                                String nome,
                                String cpf,
                                UUID idCondicao,
                                String telefone,
                                String endereco,
                                String observacao) {
}
