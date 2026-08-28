package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PessoaCondicaoRequestDto(
        @NotBlank(message = "Informar nome da justificativa")
        String nome) {
}
