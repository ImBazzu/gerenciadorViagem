package br.com.imbazzu.frontSpring.dto;

import java.util.UUID;

public record DadosPessoaisResponseDto(UUID id,
                                       String nome,
                                       String cpf,
                                       String telefone) {
}
