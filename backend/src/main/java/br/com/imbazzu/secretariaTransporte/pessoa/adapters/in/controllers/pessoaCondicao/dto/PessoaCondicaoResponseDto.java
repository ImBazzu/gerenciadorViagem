package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto;

import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoInfoOutputDto;

import java.util.List;
import java.util.UUID;

public record PessoaCondicaoResponseDto(
        UUID id,
        String descricao) {


}
